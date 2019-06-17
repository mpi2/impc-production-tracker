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
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.ProjectRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectServiceImpl implements ProjectService
{
    private ProjectRepository projectRepository;

    ProjectServiceImpl(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
    }

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
}
