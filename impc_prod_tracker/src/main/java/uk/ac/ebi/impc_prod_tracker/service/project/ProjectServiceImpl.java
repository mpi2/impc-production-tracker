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
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.impc_prod_tracker.common.history.HistoryService;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.ResourceAccessChecker;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.web.controller.project.ProjectSpecs;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.ProjectRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;
    private HistoryService<Project> historyService;
    private ProjectSpecs projectSpecs;
    private ResourceAccessChecker<Project> resourceAccessChecker;

    public ProjectServiceImpl(
        ProjectRepository projectRepository,
        HistoryService<Project> historyService,
        ProjectSpecs projectSpecs,
        ResourceAccessChecker resourceAccessChecker)
    {
        this.projectRepository = projectRepository;
        this.historyService = historyService;
        this.projectSpecs = projectSpecs;
        this.resourceAccessChecker = resourceAccessChecker;
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Project> getCurrentUserProjects(
        Pageable pageable,
        List<String> consortiaNames,
        List<String> statusesNames,
        List<String> privaciesNames)
    {
        Specification<Project> specifications =
            Specification.where(projectSpecs.withPlansInUserWorkUnits());
        specifications =
            specifications.and(
                buildSpecificationsWithCriteria(consortiaNames, statusesNames, privaciesNames));
        return projectRepository.findAll(specifications, pageable);
    }

    @Override
    public Project getCurrentUserProjectByTpn(String tpn)
    {
        Project project = null;
        Specification<Project> specifications =
            Specification.where(projectSpecs.withPlansInUserWorkUnits());
        List<Project> projects = projectRepository.findAll(specifications);
        if (projects != null)
        {
            project = projects.stream().filter(x -> tpn.equals(x.getTpn())).findFirst().orElse(null);
        }
        return project;
    }

    @Override
    public Page<Project> getProjects(
        Pageable pageable,
        List<String> consortiaNames,
        List<String> statusesNames,
        List<String> privaciesNames)
    {
        Specification<Project> specifications =
            buildSpecificationsWithCriteria(consortiaNames, statusesNames, privaciesNames);
        Page<Project> projects = projectRepository.findAll(specifications, pageable);
        return projects;
//        List<Project> filteredProjectList = getCheckedCollection(projects.getContent());
//        return new PageImpl<>(filteredProjectList, pageable, filteredProjectList.size());
    }

    private Project getAccessChecked(Project project)
    {
        return (Project) resourceAccessChecker.checkAccess(project, "READ_PROJECT");
    }

    private List<Project> getCheckedCollection(Collection<Project> projects)
    {
        return projects.stream().map(this::getAccessChecked).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Project> buildSpecificationsWithCriteria(
        List<String> consortia, List<String> statuses, List<String> privacies)
    {
        Specification<Project> specifications =
            Specification.where(projectSpecs.withConsortia(consortia))
                .and(projectSpecs.withStatuses(statuses))
                .and(projectSpecs.withPrivacies(privacies));
        return specifications;
    }

    public Set<Plan> checkCollention(Set<Plan> plans, List<String> list) {
        if (!CollectionUtils.isEmpty(list))
        {
            plans = plans.stream()
                    .filter(plan -> plan.getWorkUnit() != null
                            && list.contains(plan.getWorkUnit().getName()))
                    .collect(Collectors.toSet());
        }
        return plans;
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
