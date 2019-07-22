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
import uk.ac.ebi.impc_prod_tracker.common.Constants;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.history.HistoryDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.phenotype_plan.PhenotypePlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.phenotype_plan.PhenotypePlanDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan.ProductionPlanDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.ProductionPlanDTO;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;
import uk.ac.ebi.impc_prod_tracker.service.plan.engine.HistoryService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;

@Component
@Data
public class PlanDTOBuilder
{
    private PlanService planService;
    private HistoryService historyService;
    private ProductionPlanDTOBuilder productionPlanDTOBuilder;
    private PhenotypePlanDTOBuilder phenotypePlanDTOBuilder;
    private HistoryDTOBuilder historyDTOBuilder;

    public PlanDTOBuilder(
        PlanService planService,
        HistoryService historyService,
        ProductionPlanDTOBuilder productionPlanDTOBuilder,
        PhenotypePlanDTOBuilder phenotypePlanDTOBuilder,
        HistoryDTOBuilder historyDTOBuilder)
    {
        this.planService = planService;
        this.historyService = historyService;
        this.productionPlanDTOBuilder = productionPlanDTOBuilder;
        this.phenotypePlanDTOBuilder = phenotypePlanDTOBuilder;
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
        PlanDetailsDTO planDetailsDTO = buildPlanDetailsDTOFromPlan(plan);
        planDTO.setPlanDetailsDTO(planDetailsDTO);

        if (plan.getId() != null)
        {
            if (Constants.PRODUCTION_TYPE.equals(plan.getPlanType().getName().toLowerCase()))
            {
                ProductionPlanDTO productionPlanDTO =
                    productionPlanDTOBuilder.buildProductionPlanDTOFromPlan(plan);

                planDTO.setProductionPlanDTO(productionPlanDTO);
            }
            else
            {
                PhenotypePlanDTO phenotypePlanDTO =
                    phenotypePlanDTOBuilder.buildPhenotypePlanDTOFromPlan(plan);

                planDTO.setPhenotypePlanDTO(phenotypePlanDTO);
            }
            planDTO.setHistory(historyDTOBuilder.buildHistoryDTOBuilderFromPlan(plan));
        }

        return planDTO;
    }

    public PlanDetailsDTO buildPlanDetailsDTOFromPlan(Plan plan)
    {
        PlanDetailsDTO planDetailsDTO = new PlanDetailsDTO();
        planDetailsDTO.setPin(plan.getPin());
        if (plan.getWorkGroup() != null)
        {
            planDetailsDTO.setWorkGroupName(plan.getWorkGroup().getName());
        }
        if (plan.getPlanType() != null)
        {
            planDetailsDTO.setPlanTypeName(plan.getPlanType().getName());
        }
        if (plan.getStatus() != null)
        {
            planDetailsDTO.setStatusName(plan.getStatus().getName());
        }
        if (plan.getWorkUnit() != null)
        {
            planDetailsDTO.setWorkUnitName(plan.getWorkUnit().getName());
        }
        if (plan.getPrivacy() != null)
        {
            planDetailsDTO.setPrivacyName(plan.getPrivacy().getName());
        }
        if (plan.getConsortium() != null)
        {
            planDetailsDTO.setConsortiumName(plan.getConsortium().getName());
        }
        planDetailsDTO.setComments(plan.getComment());

        return planDetailsDTO;
    }
}
