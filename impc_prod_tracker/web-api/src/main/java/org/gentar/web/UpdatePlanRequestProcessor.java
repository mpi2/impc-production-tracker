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
package org.gentar.web;

import org.gentar.web.dto.plan.PlanDTO;
import org.gentar.web.dto.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.gentar.web.mapping.plan.attempt.crispr_attempt.CrisprAttemptMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.crispr_attempt.CrisprAttempt;
import org.gentar.biology.plan.Plan;

/**
 * Class in charge of analysing a PlanDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private CrisprAttemptMapper crisprAttemptMapper;

    public UpdatePlanRequestProcessor(CrisprAttemptMapper crisprAttemptMapper)
    {
        this.crisprAttemptMapper = crisprAttemptMapper;
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
            updateBasicInformation(plan, planDTO);

            if (planDTO.getCrisprAttemptDTO() != null)
            {
                setNewCrisprAttempt(plan, planDTO.getCrisprAttemptDTO());
            }
        }
        return plan;
    }

    private void setNewCrisprAttempt(Plan plan, CrisprAttemptDTO crisprAttemptDTO)
    {
        crisprAttemptDTO.setCrisprAttemptId(plan.getId());
        CrisprAttempt crisprAttempt = crisprAttemptMapper.toEntity(crisprAttemptDTO);
        crisprAttempt.setImitsMiAttemptId(plan.getCrisprAttempt().getImitsMiAttemptId());
        crisprAttempt.setPlan(plan);
        crisprAttempt.setId(plan.getId());
        plan.setCrisprAttempt(crisprAttempt);
    }

    private void updateBasicInformation(Plan plan, PlanDTO planDTO)
    {
        Boolean newIsActive = planDTO.getIsActive();
        if (newIsActive != null)
        {
            plan.setIsActive(newIsActive);
        }
        String newComment = planDTO.getComment();
        if (newComment != null)
        {
            plan.setComment(newComment);
        }
    }
}
