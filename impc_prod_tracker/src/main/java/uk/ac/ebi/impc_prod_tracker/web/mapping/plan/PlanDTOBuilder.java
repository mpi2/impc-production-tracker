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
package uk.ac.ebi.impc_prod_tracker.web.mapping.plan;

import lombok.Data;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.history.HistoryService;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.common.history.HistoryDTOBuilder;

@Component
@Data
public class PlanDTOBuilder
{
    private PlanService planService;
    private HistoryService historyService;
    private HistoryDTOBuilder historyDTOBuilder;

    public PlanDTOBuilder(
        PlanService planService,
        HistoryService historyService,
        HistoryDTOBuilder historyDTOBuilder)
    {
        this.planService = planService;
        this.historyService = historyService;
        this.historyDTOBuilder = historyDTOBuilder;
    }

    public PlanDTO buildPlanDTOFromPlanPid(String pin)
    {
        Plan plan = planService.getPlanByPin(pin);
        if (plan == null)
        {
            throw new OperationFailedException(
                String.format("The plan %s does not exist", pin));
        }
        return buildPlanDTOFromPlan(plan);
    }

    public PlanDTO buildPlanDTOFromPlan(Plan plan)
    {
        PlanDTO planDTO = new PlanDTO();
        planDTO.setPin(plan.getPin());
        if (plan.getWorkGroup() != null)
        {
            planDTO.setWorkGroupName(plan.getWorkGroup().getName());
        }
        if (plan.getPlanType() != null)
        {
            planDTO.setPlanTypeName(plan.getPlanType().getName());
        }
        if (plan.getStatus() != null)
        {
            planDTO.setStatusName(plan.getStatus().getName());
        }
        if (plan.getWorkUnit() != null)
        {
            planDTO.setWorkUnitName(plan.getWorkUnit().getName());
        }
        if (plan.getPrivacy() != null)
        {
            planDTO.setPrivacyName(plan.getPrivacy().getName());
        }
        if (plan.getConsortium() != null)
        {
            planDTO.setConsortiumName(plan.getConsortium().getName());
        }
        planDTO.setComments(plan.getComment());
        if (plan.getAttempt() != null)
        {
            planDTO.setPlanTypeName(plan.getAttempt().getAttemptType().getName());

        }
        return planDTO;
    }
}
