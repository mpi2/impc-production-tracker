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

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.ortholog.Ortholog;
import org.gentar.biology.ortholog.OrthologService;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanValidator;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.engine.ProjectCreator;
import org.gentar.biology.project.engine.ProjectUpdater;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadOrthologDto;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.specs.ProjectSpecs;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final HistoryService<Project> historyService;
    private final ProjectCreator projectCreator;
    private final OrthologService orthologService;
    private final ProjectQueryHelper projectQueryHelper;
    private final ProjectUpdater projectUpdater;
    private final ProjectValidator projectValidator;
    private final PlanValidator planValidator;

    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        HistoryService<Project> historyService,
        ProjectCreator projectCreator,
        OrthologService orthologService,
        ProjectQueryHelper projectQueryHelper,
        ProjectUpdater projectUpdater,
        ProjectValidator projectValidator,
        PlanValidator planValidator) {
        this.projectRepository = projectRepository;
        this.historyService = historyService;
        this.projectCreator = projectCreator;
        this.orthologService = orthologService;
        this.projectQueryHelper = projectQueryHelper;
        this.projectUpdater = projectUpdater;
        this.projectValidator = projectValidator;
        this.planValidator = planValidator;
    }

    @Override
    public Project getNotNullProjectByTpn(String tpn) {
        Project project = projectRepository.findByTpn(tpn);
        if (project == null) {
            throw new NotFoundException("Project " + tpn + " does not exist.");
        }
        projectValidator.validateReadPermissions(project);
        addOrthologs(List.of(project));
        return projectValidator.getAccessChecked(project);
    }

    @Override
    public Project getProjectByPinWithoutCheckPermissions(String tpn) {
        return projectRepository.findByTpn(tpn);
    }

    @Override
    public List<Project> getProjects(ProjectFilter projectFilter) {
        Specification<Project> specifications = buildSpecificationsWithCriteria(projectFilter);
        List<Project> projects = projectRepository.findAll(specifications);
        addOrthologs(projects);
        return projectValidator.getCheckedCollection(projects);
    }

    @Override
    public List<Project> getProjectsWithoutOrthologs(ProjectFilter projectFilter) {
        Specification<Project> specifications = buildSpecificationsWithCriteria(projectFilter);
        List<Project> projects = projectRepository.findAll(specifications);
        return projectValidator.getCheckedCollection(projects);
    }

    @Override
    public Page<Project> getProjects(ProjectFilter projectFilter, Pageable pageable) {
        Specification<Project> specifications = buildSpecificationsWithCriteria(projectFilter);
        Page<Project> projectsPage = projectRepository.findAll(specifications, pageable);
        addOrthologs(projectsPage.getContent());
        return projectsPage.map(projectValidator::getAccessChecked);
    }

    private void addOrthologs(List<Project> projects) {
        List<String> accIds = new ArrayList<>();
        projects.forEach(x -> accIds.addAll(projectQueryHelper.getAccIdsByProject(x)));
        Map<String, List<Ortholog>> orthologs = getCachedOrthologsByAccIds(accIds);
        List<ProjectIntentionGene> projectIntentionGenes = getIntentionGenesByProjects(projects);
        projectIntentionGenes.forEach(i -> {
            String accId = i.getGene().getAccId();
            List<Ortholog> orthologsByAccId = orthologs.get(accId);
            i.setAllOrthologs(orthologsByAccId);
            i.setBestOrthologs(calculateBestOrthologs(orthologsByAccId));
        });
    }

    private List<Ortholog> calculateBestOrthologs(List<Ortholog> orthologsByAccId) {
        return orthologService.calculateBestOrthologs(orthologsByAccId);
    }

    private List<ProjectIntentionGene> getIntentionGenesByProjects(List<Project> projects) {
        List<ProjectIntentionGene> projectIntentionGenes = new ArrayList<>();
        projects.forEach(x -> projectIntentionGenes.addAll(projectQueryHelper.getIntentionGenesByProject(x)));
        return projectIntentionGenes;
    }

    private Specification<Project> buildSpecificationsWithCriteria(ProjectFilter projectFilter) {
        Specification<Project> spec = ProjectSpecs.withTpns(projectFilter.getTpns());
        spec = combineSpec(spec, ProjectSpecs.withMarkerSymbols(projectFilter.getMarkerSymbols()));
        spec = combineSpec(spec, ProjectSpecs.withMarkerSymbolOrAccId(projectFilter.getGenes()));
        spec = combineSpec(spec, ProjectSpecs.withIntentions(projectFilter.getIntentions()));
        spec = combineSpec(spec, ProjectSpecs.withPlansInWorkUnitsNames(projectFilter.getWorkUnitNames()));
        spec = combineSpec(spec, ProjectSpecs.withPlansInWorkGroupNames(projectFilter.getWorGroupNames()));
        spec = combineSpec(spec, ProjectSpecs.withConsortia(projectFilter.getConsortiaNames()));
        spec = combineSpec(spec, ProjectSpecs.withAssignments(projectFilter.getAssginmentNames()));
        spec = combineSpec(spec, ProjectSpecs.withSummaryStatuses(projectFilter.getSummaryStatusNames()));
        spec = combineSpec(spec, ProjectSpecs.withPrivacies(projectFilter.getPrivaciesNames()));
        spec = combineSpec(spec, ProjectSpecs.withImitsMiPlans(projectFilter.getImitsMiPlans()));
        spec = combineSpec(spec, ProjectSpecs.withProductionColonyNames(projectFilter.getProductionColonyNames()));
        spec = combineSpec(spec, ProjectSpecs.withPhenotypingExternalRefNames(projectFilter.getPhenotypingExternalRefs()));
        spec = combineSpec(spec, ProjectSpecs.withoutNullGenesSymbols());
        return spec;
    }

    private Specification<Project> combineSpec(Specification<Project> base, Specification<Project> additional) {
        if (base == null) {
            return additional;
        }
        if (additional == null) {
            return base;
        }
        return base.and(additional);
    }

    @Override
    public Project createProject(Project project) {
        return projectCreator.createProject(project);
    }

    @Override
    public History updateProject(Project oldProject, Project newProject) {
        projectValidator.validatePrivacyData(oldProject, newProject);
        return projectUpdater.updateProject(oldProject, newProject);
    }

    @Override
    public List<History> getProjectHistory(Project project) {
        return historyService.getHistoryByEntityNameAndEntityId("Project", project.getId());
    }

    @Override
    public void checkForUpdates(Project project) {
        projectUpdater.updateProjectWhenPlanUpdated(project);
    }

    @Override
    public void associatePlanToProject(Plan plan, Project project) {
        if (project == null) {
            throw new UserOperationFailedException(
                "The plan cannot be associated with the project because the project is null");
        }
        if (plan == null) {
            throw new UserOperationFailedException(
                "The plan cannot be associated with the project because the plan is null");
        }

        planValidator.validate(plan);
        projectValidator.validateProductionAttempt(project, plan);

        project.addPlan(plan);
        plan.setProject(project);
    }

    @Override
    public List<Outcome> getProductionOutcomesByProject(Project project) {
        List<Outcome> productionOutcomes = new ArrayList<>();
        List<Plan> productionPlans =
            projectQueryHelper.getPlansByType(project, PlanTypeName.PRODUCTION);
        if (productionPlans != null) {
            productionPlans.forEach(x -> {
               if(!x.getAttemptType().getName().contains("modification")) {
                   Set<Outcome> outcomes = x.getOutcomes();
                   productionOutcomes.addAll(outcomes);
               }
            });
        }
        return productionOutcomes;
    }

    @Override
    public Plan getFirstProductionPlan(Project project) {
        return project.getPlans().stream().min(Comparator.comparing(Plan::getId))
            .get();
    }


    private Map<String, List<Ortholog>> getCachedOrthologsByAccIds(List<String> mgiIds) {

        List<ProjectSearchDownloadOrthologDto> cachedOrthologs = orthologService.getOrthologs(mgiIds);

        return orthologService.formatOrthologs(cachedOrthologs);

    }

}
