/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package uk.ac.ebi.impc_prod_tracker.service.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.history.HistoryService;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.ResourceAccessChecker;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.ProjectRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Transactional
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;
    private HistoryService<Project> historyService;
    private ResourceAccessChecker<Project> resourceAccessChecker;

    public static final String READ_PROJECT_ACTION = "READ_PROJECT";

    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        HistoryService<Project> historyService,
        ResourceAccessChecker resourceAccessChecker)
    {
        this.projectRepository = projectRepository;
        this.historyService = historyService;
        this.resourceAccessChecker = resourceAccessChecker;
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public Project getProjectByTpn(String tpn)
    {
        Specification<Project> specifications = ProjectSpecs.withTpns(Arrays.asList(tpn));
        Project project = projectRepository.findOne(specifications).orElse(null);
        return getAccessChecked(project);
    }

    @Override
    public Page<Project> getProjects(
        Pageable pageable,
        List<String> workUnitNames,
        List<String> consortiaNames,
        List<String> statusesNames,
        List<String> privaciesNames)
    {
          Specification<Project> specifications =
              buildSpecificationsWithCriteria(
                  workUnitNames, consortiaNames, statusesNames, privaciesNames);
        List<Project> projects = projectRepository.findAll(specifications);
        return getAccessCheckedPage(projects, pageable);
    }

    private Page<Project> getAccessCheckedPage(List<Project> projects, Pageable pageable)
    {

        List<Project> filteredProjectList = getCheckedCollection(projects);
        return new PageImpl<>(
            filteredProjectList, pageable, filteredProjectList.size());
    }

    private Project getAccessChecked(Project project)
    {
        return (Project) resourceAccessChecker.checkAccess(project, READ_PROJECT_ACTION);
    }

    private List<Project> getCheckedCollection(Collection<Project> projects)
    {
        return projects.stream().map(this::getAccessChecked).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Project> buildSpecificationsWithCriteria(
        List<String> workUnitNames,
        List<String> consortia,
        List<String> statuses,
        List<String> privacies)
    {
        Specification<Project> specifications =
            Specification.where(
                ProjectSpecs.withPlansInWorkUnitsNames(workUnitNames)
                .and(ProjectSpecs.withConsortia(consortia))
                .and(ProjectSpecs.withStatuses(statuses))
                .and(ProjectSpecs.withPrivacies(privacies)));
        return specifications;
    }

    @Override
    public Project createProject(NewProjectRequestDTO newProjectRequestDTO)
    {
        Project p = new Project();

        p.setTpn("TPN:");
        em.persist(p);
        String tpn = createIdentifier(p.getId(), p.getTpn(), 9);
        p.setTpn(tpn);
        em.close();
        projectRepository.save(p);

        return p;
    }

    private String createIdentifier(Long id, String name, int length){
        String identifier = String.format("%0" + length + "d", id);
        identifier = name + identifier;
        return identifier;
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
