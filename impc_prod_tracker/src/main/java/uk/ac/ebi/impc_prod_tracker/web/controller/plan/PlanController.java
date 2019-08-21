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
package uk.ac.ebi.impc_prod_tracker.web.controller.plan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.UpdatePlanRequestDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.PlanDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.web.mapping.common.history.HistoryMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectPlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.common.history.HistoryDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.common.history.HistoryDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PlanController
{
    private PlanDTOBuilder planDTOBuilder;
    private ProjectDTOBuilder projectDTOBuilder;
    private HistoryMapper historyMapper;

    public PlanController(
        PlanDTOBuilder planDTOBuilder,
        ProjectDTOBuilder projectDTOBuilder,
        HistoryMapper historyMapper)
    {
        this.planDTOBuilder = planDTOBuilder;
        this.projectDTOBuilder = projectDTOBuilder;
        this.historyMapper = historyMapper;
    }

    @GetMapping(value = {"/plans"})
    public List<ProjectPlanDTO> getPlans()
    {
        List<Plan> plans = projectDTOBuilder.getPlanService().getPlans();

        List<ProjectPlanDTO> projectPlanDTOS = new ArrayList<>();
        for (Plan plan : plans)
        {
            ProjectPlanDTO projectPlanDTO = new ProjectPlanDTO();
            PlanDTO planDTO = planDTOBuilder.buildPlanDTOFromPlan(plan);

            projectPlanDTO.setPlanDTO(planDTO);
            projectPlanDTO.setProjectDetailsDTO(
                projectDTOBuilder.buildProjectDetailsDTOFromProject(plan.getProject()));
            projectPlanDTOS.add(projectPlanDTO);
        }

        return projectPlanDTOS;
    }

    @GetMapping(value = {"/plans/{pin}"})
    public ProjectPlanDTO getPlan(@PathVariable String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);

        ProjectDetailsDTO projectDetailsDTO =
            projectDTOBuilder.buildProjectDetailsDTOFromProject(plan.getProject());
        ProjectPlanDTO projectPlanDTO =  new ProjectPlanDTO();

        projectPlanDTO.setProjectDetailsDTO(projectDetailsDTO);
        projectPlanDTO.setPlanDTO(planDTOBuilder.buildPlanDTOFromPlan(plan));

        return projectPlanDTO;
    }

    @GetMapping(value = {"/plans/{pin}/history"})
    public List<HistoryDTO> getPlanHistory(@PathVariable String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);

        return historyMapper.toDtos(projectDTOBuilder.getPlanService().getPlanHistory(plan));
    }

    private Plan getNotNullPlanByPin(String pin)
    {
        Plan plan = projectDTOBuilder.getPlanService().getPlanByPin(pin);
        if (plan == null)
        {
            throw new OperationFailedException(
                String.format("Plan %s does not exist.", pin), HttpStatus.NOT_FOUND);
        }
        return plan;
    }

    @PutMapping(value = {"/plans/{pin}"})
    public HistoryDTO updatePlan(
        @PathVariable String pin, @RequestBody UpdatePlanRequestDTO updatePlanRequestDTO)
    {
        History history = projectDTOBuilder.getPlanService().updatePlan(pin, updatePlanRequestDTO);
        if (history != null)
        {
            return  historyMapper.toDto(history);
        }
        return null;
    }
}
