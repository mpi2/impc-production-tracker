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

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.mappers.PlanUpdateMapper;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;

/**
 * Class in charge of analysing a PlanDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private final PlanUpdateMapper planUpdateMapper;
    private final PlanService planService;
    private final FunderMapper funderMapper;

    public UpdatePlanRequestProcessor(PlanUpdateMapper planUpdateMapper,
                                      PlanService planService,
                                      FunderMapper funderMapper)
    {
        this.planUpdateMapper = planUpdateMapper;
        this.planService = planService;
        this.funderMapper = funderMapper;
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

            plan.setFunders(mappedPlan.getFunders());
            plan.setComment(mappedPlan.getComment());
            setUpdatedCrisprAttempt(plan, mappedPlan);
            setUpdatedPhenotypingAttempt(plan, mappedPlan);
            setUpdatedEsCellAttempt(plan, mappedPlan);
            setUpdatedEsCellAlleleModificationAttempt(plan, mappedPlan);
            setUpdatedCrisprAlleleModificationAttempt(plan, mappedPlan);
            setEvent(plan, planUpdateDTO);
        }
        return plan;
    }

    private void setUpdatedEsCellAlleleModificationAttempt(Plan originalPlan, Plan mappedPlan)
    {
        if (mappedPlan.getEsCellAlleleModificationAttempt() != null)
        {
            originalPlan.setEsCellAlleleModificationAttempt(mappedPlan.getEsCellAlleleModificationAttempt());
        }
    }

    private void setUpdatedCrisprAlleleModificationAttempt(Plan originalPlan, Plan mappedPlan)
    {
        if (mappedPlan.getCrisprAlleleModificationAttempt() != null)
        {
            originalPlan.setCrisprAlleleModificationAttempt(mappedPlan.getCrisprAlleleModificationAttempt());
        }
    }


    private void setUpdatedEsCellAttempt(Plan originalPlan, Plan mappedPlan)
    {
        if (mappedPlan.getEsCellAttempt() != null)
        {
            originalPlan.setEsCellAttempt(mappedPlan.getEsCellAttempt());
        }
    }

    private void setUpdatedCrisprAttempt(Plan originalPlan, Plan mappedPlan)
    {
        if (mappedPlan.getCrisprAttempt() != null)
        {
            originalPlan.setCrisprAttempt(mappedPlan.getCrisprAttempt());
        }
    }

    // Update only desired fields
    private void setUpdatedPhenotypingAttempt(Plan originalPlan, Plan mappedPlan)
    {
        if (mappedPlan.getPhenotypingAttempt() != null)
        {
            PhenotypingAttempt phenotypingAttempt = mappedPlan.getPhenotypingAttempt();
            if (originalPlan.getPhenotypingAttempt() != null)
            {
                phenotypingAttempt =
                    new PhenotypingAttempt(originalPlan.getPhenotypingAttempt());
            }
            PhenotypingAttempt mapedPhenotypingAttempt = mappedPlan.getPhenotypingAttempt();

            phenotypingAttempt.setDoNotCountTowardsCompleteness(
                mapedPhenotypingAttempt.getDoNotCountTowardsCompleteness());

            phenotypingAttempt.setPhenotypingExternalRef(
                mapedPhenotypingAttempt.getPhenotypingExternalRef());

            phenotypingAttempt.setStrain(mapedPhenotypingAttempt.getStrain());

            phenotypingAttempt.setCohortWorkUnit(mapedPhenotypingAttempt.getCohortWorkUnit());

            originalPlan.setPhenotypingAttempt(phenotypingAttempt);
        }
    }

    private void setEvent(Plan plan, PlanUpdateDTO planUpdateDTO)
    {
        String action = null;
        if (planUpdateDTO.getStatusTransitionDTO() != null)
        {
            action = planUpdateDTO.getStatusTransitionDTO().getActionToExecute();
        }
        if (action != null)
        {
            ProcessEvent processEvent = planService.getProcessEventByName(plan, action);
            plan.setProcessDataEvent(processEvent);
        }
    }
}
