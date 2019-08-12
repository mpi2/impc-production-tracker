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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.ac.ebi.impc_prod_tracker.common.history.HistoryService;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.NewProjectRequestDTO;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.ProjectRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;
    private HistoryService<Project> historyService;

    public ProjectServiceImpl(
        ProjectRepository projectRepository, HistoryService<Project> historyService)
    {
        this.projectRepository = projectRepository;
        this.historyService = historyService;
    }

    @PersistenceContext
    private EntityManager em;



    @Override
    public List<Project> getProjects()
    {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectByTpn(String tpn)
    {
        Project project = projectRepository.findProjectByTpn(tpn);
        return project;
    }

    @Override
    public Page<Project> getProjectsBySpecPro(
        Specification<Project> specification, Pageable pageable)
    {
        return projectRepository.findAll(specification, pageable);
    }

    @Override
    public Project getProjectFilteredByPlanAttributes(
        Project project,
        List<String> workUnits,
        List<String> workGroups,
        List<String> planTypes,
        List<String> statuses,
        List<String> privacies)
    {
        Set<Plan> plans = project.getPlans();

//        plans = checkCollention(plans, workUnits);

        if (!CollectionUtils.isEmpty(workUnits))
        {
            plans = plans.stream()
                .filter(plan -> plan.getWorkUnit() != null
                    && workUnits.contains(plan.getWorkUnit().getName()))
                .collect(Collectors.toSet());
        }
        if (!CollectionUtils.isEmpty(workGroups))
        {
            plans = plans.stream()
                .filter(plan -> plan.getWorkGroup() != null
                    && workGroups.contains(plan.getWorkGroup().getName()))
                .collect(Collectors.toSet());
        }
        if (!CollectionUtils.isEmpty(planTypes))
        {
            plans = plans.stream()
                .filter(plan -> plan.getPlanType() != null
                    && planTypes.contains(plan.getPlanType().getName()))
                .collect(Collectors.toSet());
        }
        if (!CollectionUtils.isEmpty(statuses))
        {
            plans = plans.stream()
                .filter(plan -> plan.getStatus() != null
                    && statuses.contains(plan.getStatus().getName()))
                .collect(Collectors.toSet());
        }

        if (!CollectionUtils.isEmpty(privacies))
        {
            plans = plans.stream()
                .filter(plan -> plan.getPrivacy() != null
                    && privacies.contains(plan.getPrivacy().getName()))
                .collect(Collectors.toSet());
        }

        project.setPlans(plans);
        return project;
    }

//    public Set<Plan> checkCollention(Set<Plan> plans, List<String> list) {
//        if (!CollectionUtils.isEmpty(list))
//        {
//            plans = plans.stream()
//                    .filter(plan -> plan.getWorkUnit() != null
//                            && list.contains(plan.getWorkUnit().getName()))
//                    .collect(Collectors.toSet());
//        }
//        return plans;
//    }

    @Override
    public Project createProject(NewProjectRequestDTO newProjectRequestDTO)
    {
        Project p = new Project();


        p.setTpn("TPN:");
        em.persist(p);
        String tpn = createIdentifier(p.getId(), p.getTpn(), 9);
        p.setTpn(tpn);
        em.close();
//        projectRepository.save(p);


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
        return historyService.getHistoryByEntityNameAndEntityId("project", project.getId());
    }
}
