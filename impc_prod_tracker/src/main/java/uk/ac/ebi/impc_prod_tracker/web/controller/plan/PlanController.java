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
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.web.dto.common.history.HistoryDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.common.history.HistoryMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.PlanMapper;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PlanController
{
    private HistoryMapper historyMapper;
    private PlanService planService;
    private PlanMapper planMapper;

    public PlanController(HistoryMapper historyMapper, PlanService planService, PlanMapper planMapper)
    {
        this.historyMapper = historyMapper;
        this.planService = planService;
        this.planMapper = planMapper;
    }

    @GetMapping(value = {"/plans"})
    public List<PlanDTO> getPlans()
    {
        return planMapper.toDtos(planService.getPlans());
    }

    @GetMapping(value = {"/plans/{pin}"})
    public PlanDTO getPlan(@PathVariable String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);

        PlanDTO planDTO = planMapper.toDto(plan);

        return planDTO;
    }

    @GetMapping(value = {"/plans/{pin}/history"})
    public List<HistoryDTO> getPlanHistory(@PathVariable String pin)
    {
        Plan plan = getNotNullPlanByPin(pin);

        return historyMapper.toDtos(planService.getPlanHistory(plan));
    }

    private Plan getNotNullPlanByPin(String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        return plan;
    }

    @PutMapping(value = {"/plans/{pin}"})
    public HistoryDTO updatePlan(
        @PathVariable String pin, @RequestBody PlanDTO planDTO)
    {
        History history = planService.updatePlan(pin, planDTO);
        if (history != null)
        {
            return  historyMapper.toDto(history);
        }
        return null;
    }
}
