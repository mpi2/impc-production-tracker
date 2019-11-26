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
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.audit.history.History;
import org.gentar.biology.project.engine.ProjectCreator;
import org.gentar.biology.project.assignment_status.AssignmentStatus;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;
    private HistoryService<Project> historyService;
    private ResourceAccessChecker<Project> resourceAccessChecker;
    private ProjectCreator projectCreator;

    public static final String READ_PROJECT_ACTION = "READ_PROJECT";

    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        HistoryService<Project> historyService,
        ResourceAccessChecker resourceAccessChecker,
        ProjectCreator projectCreator)
    {
        this.projectRepository = projectRepository;
        this.historyService = historyService;
        this.resourceAccessChecker = resourceAccessChecker;
        this.projectCreator = projectCreator;
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
        return getCheckedCollection(projects);
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
}
