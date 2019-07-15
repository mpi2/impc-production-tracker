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
package uk.ac.ebi.impc_prod_tracker.controller.project;

import lombok.Data;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Data
public class ProjectDTOBuilder
{
    private PlanService planService;
    private PlanDTOBuilder planDTOBuilder;
    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";

    public ProjectDTOBuilder(
        PlanService planService, PlanDTOBuilder planDTOBuilder)
    {
        this.planService = planService;
        this.planDTOBuilder = planDTOBuilder;
    }

    public ProjectDTO buildProjectDTOFromProject(Project project)
    {
        ProjectDTO projectDTO = convertToDto(project);
        List<Plan> plans = planService.getPlansByProject(project);
        List<PlanDTO> planDTOS = new ArrayList<>();

        for (Plan p : plans)
        {
            PlanDTO planDTO = planDTOBuilder.buildPlanDTOFromPlan(p);
            planDTOS.add(planDTO);

        }
        projectDTO.setPlans(planDTOS);

        return projectDTO;
    }

    private ProjectDTO convertToDto(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDTO projectDTO = new ProjectDTO();
        ProjectDetailsDTO projectDetailsDTO = buildProjectDetailsDTOFromProject(project);
        projectDetailsDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        projectDetailsDTO.setTpn(project.getTpn());
        projectDTO.setProjectDetailsDTO(projectDetailsDTO);

        return projectDTO;
    }

    public ProjectDetailsDTO buildProjectDetailsDTOFromProject(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO();
        if (project.getAssignmentStatus() != null)
        {
            projectDetailsDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        }

        projectDetailsDTO.setTpn(project.getTpn());

//        if (project.getProjectPriority() != null)
//        {
//            projectDetailsDTO.setPriorityName(project.getProjectPriority().getName());
//        }
        // addMarkerSymbols(projectDetailsDTO, project);
        // addIntentions(projectDetailsDTO, project);

        return projectDetailsDTO;
    }

//    private void addMarkerSymbols(ProjectDetailsDTO projectDetailsDTO, final Project project)
//    {
//        Set<IntendedMouseGene> intendedMouseGenes = project.getIntendedMouseGenes();
//        List<ProjectDetailsDTO.MarkerSymbolDTO> markerSymbolDTOS = new ArrayList<>();
//        if (intendedMouseGenes != null)
//        {
//            for (IntendedMouseGene intendedMouseGene : intendedMouseGenes)
//            {
//                ProjectDetailsDTO.MarkerSymbolDTO markerSymbolDTO =
//                    new ProjectDetailsDTO.MarkerSymbolDTO();
//                markerSymbolDTO.setMarkerSymbol(intendedMouseGene.getSymbol());
//                markerSymbolDTO.setMgiLink(MGI_URL + intendedMouseGene.getMgiId());
//                markerSymbolDTOS.add(markerSymbolDTO);
//            }
//        }
//        projectDetailsDTO.setMarkerSymbols(markerSymbolDTOS);
//    }
//
//    private void addIntentions(ProjectDetailsDTO projectDetailsDTO, final Project project)
//    {
//        Set<AlleleType> alleleTypes = project.getProjectIntentions();
//        List<String> intentions = new ArrayList<>();
//        if (alleleTypes != null)
//        {
//            for (AlleleType alleleType : alleleTypes)
//            {
//                intentions.add(alleleType.getName());
//            }
//        }
//        projectDetailsDTO.setAlleleIntentions(intentions);
//    }
}
