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

import org.gentar.biology.plan.mappers.PlanMapper;
import org.springframework.stereotype.Component;

/**
 * Class in charge of analysing a PlanDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private PlanMapper planMapper;

    public UpdatePlanRequestProcessor(PlanMapper planMapper)
    {
        this.planMapper = planMapper;
    }

    /**
     *
     * @param plan An Existing plan in the system.
     * @param planDTO New information for plan.
     * @return The plan object with the new information that is requested in updatePlanRequestDTO.
     * This is only modifications in the object. No database changes here.
     */
    public Plan getPlanToUpdate(Plan plan, PlanDTO planDTO)
    {
        if (planDTO != null)
        {
            Plan mappedPlan = planMapper.toEntity(planDTO);
            plan.setComment(mappedPlan.getComment());
            plan.setCrisprAttempt(mappedPlan.getCrisprAttempt());
            plan.setPhenotypingAttempt(mappedPlan.getPhenotypingAttempt());
            plan.setBreedingAttempt(mappedPlan.getBreedingAttempt());
        }
        return plan;
    }
}
