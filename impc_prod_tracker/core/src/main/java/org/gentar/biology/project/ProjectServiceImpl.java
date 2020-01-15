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
import org.gentar.biology.project.intention.project_intention.ProjectIntention;
import org.gentar.biology.project.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.project.search.filter.FluentProjectFilter;
import org.gentar.biology.project.specs.ProjectSpecs;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.audit.history.History;
import org.gentar.biology.project.engine.ProjectCreator;
import org.gentar.biology.project.assignment_status.AssignmentStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;
    private HistoryService<Project> historyService;
    private ResourceAccessChecker<Project> resourceAccessChecker;
    private ProjectCreator projectCreator;
    private OrthologService orthologService;

    public static final String READ_PROJECT_ACTION = "READ_PROJECT";

    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        HistoryService<Project> historyService,
        ResourceAccessChecker<Project> resourceAccessChecker,
        ProjectCreator projectCreator,
        OrthologService orthologService)
    {
        this.projectRepository = projectRepository;
        this.historyService = historyService;
        this.resourceAccessChecker = resourceAccessChecker;
        this.projectCreator = projectCreator;
        this.orthologService = orthologService;
    }

    @Override
    public Project getProjectByTpn(String tpn)
    {
        Specification<Project> specifications = ProjectSpecs.withTpns(Arrays.asList(tpn));
        Project project = projectRepository.findOne(specifications).orElse(null);
        return getAccessChecked(project);
    }

    public List<Project> getProjects(ProjectFilter projectFilter)
    {
        Specification<Project> specifications = buildSpecificationsWithCriteria(projectFilter);
        List<Project> projects = projectRepository.findAll(specifications);
        projects = applyFiltersToResults(projects, projectFilter);
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
        projects.forEach(x -> {
            accIds.addAll(getAccIdsByProject(x));
        });
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

    public List<String> getAccIdsByProject(Project project)
    {
        List<String> accIds = new ArrayList<>();
        List<ProjectIntentionGene> projectIntentionGenes = getIntentionGenesByProject(project);
        projectIntentionGenes.forEach(x -> {
            accIds.add(x.getGene().getAccId());
        });
        return accIds;
    }

    private List<ProjectIntentionGene> getIntentionGenesByProjects(List<Project> projects)
    {
        List<ProjectIntentionGene> projectIntentionGenes = new ArrayList<>();
        projects.forEach(x -> {
            projectIntentionGenes.addAll(getIntentionGenesByProject(x));
        });
        return projectIntentionGenes;
    }

    public List<ProjectIntentionGene> getIntentionGenesByProject(Project project)
    {
        List<ProjectIntentionGene> projectIntentionGenes = new ArrayList<>();
        List<ProjectIntention> intentions = project.getProjectIntentions();
        if (intentions != null)
        {
            intentions.forEach(x -> {
                ProjectIntentionGene intentionGene = x.getProjectIntentionGene();
                if (intentionGene != null)
                {
                    projectIntentionGenes.add(intentionGene);
                }
            });
        }
        return projectIntentionGenes;
    }

    private Project getAccessChecked(Project project)
    {
        return (Project) resourceAccessChecker.checkAccess(project, READ_PROJECT_ACTION);
    }

    private List<Project> getCheckedCollection(Collection<Project> projects)
    {
        return projects.stream().map(this::getAccessChecked).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Project> buildSpecificationsWithCriteria(ProjectFilter projectFilter)
    {
        Specification<Project> specifications =
            Specification.where(
                ProjectSpecs.withTpns(projectFilter.getTpns())
                .and(ProjectSpecs.withExternalReferences(projectFilter.getExternalReferences()))
                .and(ProjectSpecs.withMarkerSymbols(projectFilter.getMarkerSymbols()))
                .and(ProjectSpecs.withGenes(projectFilter.getGenes()))
                .and(ProjectSpecs.withIntentions(projectFilter.getIntentions()))
                .and(ProjectSpecs.withPlansInWorkUnitsNames(projectFilter.getWorkUnitNames()))
                .and(ProjectSpecs.withPlansInWorkGroupNames(projectFilter.getWorGroupNames()))
                .and(ProjectSpecs.withConsortia(projectFilter.getConsortiaNames()))
                .and(ProjectSpecs.withStatuses(projectFilter.getStatusesNames()))
                .and(ProjectSpecs.withPrivacies(projectFilter.getPrivaciesNames())));
        return specifications;
    }

    @Override
    public Project createProject(Project project)
    {
        return projectCreator.createProject(project);
    }

    private AssignmentStatus checkIfAnyProjectAlreadyCreated()
    {

        return null;
    }

    @Override
    public List<History> getProjectHistory(Project project)
    {
        return historyService.getHistoryByEntityNameAndEntityId("Project", project.getId());
    }

    private List<Project> applyFiltersToResults(List<Project> allResults, ProjectFilter filters)
    {
        FluentProjectFilter fluentProjectFilter = new FluentProjectFilter(allResults);
        return fluentProjectFilter
            .withTpns(filters.getTpns())
            .withWorkUnitNames(filters.getWorkUnitNames())
            .withWorkGroupNames(filters.getWorGroupNames())
            .withExternalReference(filters.getExternalReferences())
            .withAlleleTypeNames(filters.getIntentions())
            .withConsortiaNames(filters.getConsortiaNames())
            .withPrivaciesNames(filters.getPrivaciesNames()).getFilteredData();
    }
}
