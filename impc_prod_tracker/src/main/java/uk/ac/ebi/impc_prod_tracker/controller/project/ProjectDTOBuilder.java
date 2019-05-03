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
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Data
public class ProjectDTOBuilder
{
    private ProjectMapper projectMapper;
    private PlanService planService;
    private PlanDTOBuilder planDTOBuilder;

    public ProjectDTOBuilder(
        ProjectMapper projectMapper, PlanService planService, PlanDTOBuilder planDTOBuilder)
    {
        this.projectMapper = projectMapper;
        this.planService = planService;
        this.planDTOBuilder = planDTOBuilder;
    }

    public ProjectDTO buildProjectDTOFromProject(Project project)
    {
        ProjectDTO projectDTO = projectMapper.convertToDto(project);
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

    private void addMarkerSymbols(ProjectDTO projectDTO, Project project)
    {
        Set<IntendedMouseGene> intendedMouseGenes = project.getIntendedMouseGenes();
        List<String> markerSymbolNames = new ArrayList<>();
        for (IntendedMouseGene intendedMouseGene : intendedMouseGenes)
        {
            markerSymbolNames.add(intendedMouseGene.getSymbol());
        }
        //TODO: Finish method
    }
}
