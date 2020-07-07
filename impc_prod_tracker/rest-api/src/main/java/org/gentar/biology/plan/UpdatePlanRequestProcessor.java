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
package org.gentar.biology.plan;

import org.gentar.biology.plan.mappers.PlanUpdateMapper;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

/**
 * Class in charge of analysing a PlanDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private PlanUpdateMapper planUpdateMapper;
    private PlanService planService;

    public UpdatePlanRequestProcessor(PlanUpdateMapper planUpdateMapper, PlanService planService)
    {
        this.planUpdateMapper = planUpdateMapper;
        this.planService = planService;
    }

    /**
     *
     * @param plan An Existing plan in the system.
     * @param planUpdateDTO New information for plan.
     * @return The plan object with the new information that is requested in updatePlanRequestDTO.
     * This is only modifications in the object. No database changes here.
     */
    public Plan getPlanToUpdate(Plan plan, PlanUpdateDTO planUpdateDTO)
    {
        if (planUpdateDTO != null)
        {
            // Assign id to the dto to propagate it where needed
            planUpdateDTO.setId(plan.getId());

            Plan mappedPlan = planUpdateMapper.toEntity(planUpdateDTO);
            plan.setComment(mappedPlan.getComment());
            plan.setProductsAvailableForGeneralPublic(mappedPlan.getProductsAvailableForGeneralPublic());
            if (mappedPlan.getCrisprAttempt() != null)
            {
                plan.setCrisprAttempt(mappedPlan.getCrisprAttempt());
            }
            if (mappedPlan.getPhenotypingAttempt() != null)
            {
                plan.setPhenotypingAttempt(mappedPlan.getPhenotypingAttempt());
            }
            if (mappedPlan.getBreedingAttempt() != null)
            {
                plan.setBreedingAttempt(mappedPlan.getBreedingAttempt());
            }
            setEvent(plan, planUpdateDTO);
        }
        return plan;
    }

    private void setEvent(Plan plan, PlanUpdateDTO planUpdateDTO)
    {
        String action = planUpdateDTO.getStatusTransitionDTO().getActionToExecute();
        if (action != null)
        {
            ProcessEvent processEvent = planService.getProcessEventByName(plan, action);
            plan.setEvent(processEvent);
        }
    }
}
