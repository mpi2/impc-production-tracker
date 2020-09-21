/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.project;

import org.gentar.audit.history.HistoryService;
import org.gentar.biology.ortholog.Ortholog;
import org.gentar.biology.ortholog.OrthologService;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.engine.ProjectUpdater;
import org.gentar.biology.project.specs.ProjectSpecs;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.audit.history.History;
import org.gentar.biology.project.engine.ProjectCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectServiceImpl implements ProjectService
{
    private final ProjectRepository projectRepository;
    private final HistoryService<Project> historyService;
    private final ResourceAccessChecker<Project> resourceAccessChecker;
    private final ProjectCreator projectCreator;
    private final OrthologService orthologService;
    private final ProjectQueryHelper projectQueryHelper;
    private final ProjectUpdater projectUpdater;

    public static final String READ_PROJECT_ACTION = "READ_PROJECT";

    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        HistoryService<Project> historyService,
        ResourceAccessChecker<Project> resourceAccessChecker,
        ProjectCreator projectCreator,
        OrthologService orthologService,
        ProjectQueryHelper projectQueryHelper,
        ProjectUpdater projectUpdater)
    {
        this.projectRepository = projectRepository;
        this.historyService = historyService;
        this.resourceAccessChecker = resourceAccessChecker;
        this.projectCreator = projectCreator;
        this.orthologService = orthologService;
        this.projectQueryHelper = projectQueryHelper;
        this.projectUpdater = projectUpdater;
    }

    @Override
    public Project getProjectByTpn(String tpn)
    {
        Specification<Project> specifications = ProjectSpecs.withTpns(Arrays.asList(tpn));
        Project project = projectRepository.findOne(specifications).orElse(null);
        return getAccessChecked(project);
    }

    @Override
    public Project getNotNullProjectByTpn(String tpn)
    {
        Project project = getProjectByTpn(tpn);
        if (project == null)
        {
            throw new UserOperationFailedException("Project " + tpn + " does not exist");
        }
        return project;
    }

    @Override
    public List<Project> getProjects(ProjectFilter projectFilter)
    {
        Specification<Project> specifications = buildSpecificationsWithCriteria(projectFilter);
        List<Project> projects = projectRepository.findAll(specifications);
        addOrthologs(projects);
        return getCheckedCollection(projects);
    }

    @Override
    public Page<Project> getProjects(ProjectFilter projectFilter, Pageable pageable)
    {
        Specification<Project> specifications = buildSpecificationsWithCriteria(projectFilter);
        Page<Project> projectsPage = projectRepository.findAll(specifications, pageable);
        addOrthologs(projectsPage.getContent());
        return projectsPage.map(this::getAccessChecked);
    }

    private void addOrthologs(List<Project> projects)
    {
        List<String> accIds = new ArrayList<>();
        projects.forEach(x -> accIds.addAll(projectQueryHelper.getAccIdsByProject(x)));
        Map<String, List<Ortholog>> orthologs = orthologService.getOrthologsByAccIds(accIds);
        List<ProjectIntentionGene> projectIntentionGenes = getIntentionGenesByProjects(projects);
        projectIntentionGenes.forEach(i -> {
            String accId = i.getGene().getAccId();
            List<Ortholog> orthologsByAccId = orthologs.get(accId);
            i.setAllOrthologs(orthologsByAccId);
            i.setBestOrthologs(calculateBestOrthologs(orthologsByAccId));
        });
    }

    private List<Ortholog> calculateBestOrthologs(List<Ortholog> orthologsByAccId)
    {
        return orthologService.calculateBestOrthologs(orthologsByAccId);
    }

    private List<ProjectIntentionGene> getIntentionGenesByProjects(List<Project> projects)
    {
        List<ProjectIntentionGene> projectIntentionGenes = new ArrayList<>();
        projects.forEach(x -> {
            projectIntentionGenes.addAll(projectQueryHelper.getIntentionGenesByProject(x));
        });
        return projectIntentionGenes;
    }

    private Project getAccessChecked(Project project)
    {
        return (Project) resourceAccessChecker.checkAccess(project, READ_PROJECT_ACTION);
    }

    private List<Project> getCheckedCollection(Collection<Project> projects)
    {
        return projects.stream()
            .map(this::getAccessChecked).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Project> buildSpecificationsWithCriteria(ProjectFilter projectFilter)
    {
        Specification<Project> specifications =
            Specification.where(
                ProjectSpecs.withTpns(projectFilter.getTpns())
                .and(ProjectSpecs.withExternalReferences(projectFilter.getExternalReferences()))
                .and(ProjectSpecs.withMarkerSymbols(projectFilter.getMarkerSymbols()))
                .and(ProjectSpecs.withMarkerSymbolOrAccId(projectFilter.getGenes()))
                .and(ProjectSpecs.withIntentions(projectFilter.getIntentions()))
                .and(ProjectSpecs.withPlansInWorkUnitsNames(projectFilter.getWorkUnitNames()))
                .and(ProjectSpecs.withPlansInWorkGroupNames(projectFilter.getWorGroupNames()))
                .and(ProjectSpecs.withConsortia(projectFilter.getConsortiaNames()))
                .and(ProjectSpecs.withAssignments(projectFilter.getAssginmentNames()))
                .and(ProjectSpecs.withSummaryStatuses(projectFilter.getSummaryStatusNames()))
                .and(ProjectSpecs.withPrivacies(projectFilter.getPrivaciesNames())))
                .and(ProjectSpecs.withImitsMiPlans(projectFilter.getImitsMiPlans()));
        return specifications;
    }

    @Override
    public Project createProject(Project project)
    {
        return projectCreator.createProject(project);
    }

    @Override
    public History updateProject(Project oldProject, Project newProject)
    {
        return projectUpdater.updateProject(oldProject, newProject);
    }

    @Override
    public List<History> getProjectHistory(Project project)
    {
        return historyService.getHistoryByEntityNameAndEntityId("Project", project.getId());
    }

    @Override
    public void checkForUpdates(Project project)
    {
        projectUpdater.updateProjectWhenPlanUpdated(project);
    }

    @Override
    public void associatePlanToProject(Plan plan, Project project)
    {
        if (project == null)
        {
            throw new UserOperationFailedException(
                "The plan cannot be associated with the project because the project is null");
        }
        if (plan == null)
        {
            throw new UserOperationFailedException(
                "The plan cannot be associated with the project because the plan is null");
        }
        project.addPlan(plan);
        plan.setProject(project);
    }

    @Override
    public List<Outcome> getProductionOutcomesByProject(Project project)
    {
        List<Outcome> productionOutcomes = new ArrayList<>();
        List<Plan> productionPlans =
            projectQueryHelper.getPlansByType(project, PlanTypeName.PRODUCTION);
        if (productionPlans != null)
        {
            productionPlans.forEach(x -> {
                Set<Outcome> outcomes = x.getOutcomes();
                productionOutcomes.addAll(outcomes);
            });
        }
        return productionOutcomes;
    }
}
