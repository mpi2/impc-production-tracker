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
package uk.ac.ebi.impc_prod_tracker.controller.project.plan;

import lombok.Data;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exeption_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.PhenotypePlanSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.ProductionPlanSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.f1_colony.F1ColonyDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.micro_injection.MicroInjectionDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;

@Component
@Data
public class PlanDTOBuilder
{
    private PlanService planService;

    public PlanDTOBuilder(PlanService planService)
    {
        this.planService = planService;
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

        if ("Production".equals(plan.getPlanType().getName()))
        {
            ProductionPlanSummaryDTO productionPlanSummaryDTO =
                buildProductionPlanSummaryDTOFromPlan(plan);

            planDTO.setProductionPlanSummaryDTO(productionPlanSummaryDTO);
        }
        else
        {
            PhenotypePlanSummaryDTO phenotypePlanSummaryDTO =
                buildPhenotypePlanSummaryDTOFromPlan(plan);

            planDTO.setPhenotypePlanSummaryDTO(phenotypePlanSummaryDTO);
        }

        return planDTO;
    }

    public PlanDetailsDTO buildPlanDetailsDTOFromPlan(Plan plan)
    {
        PlanDetailsDTO planDetailsDTO = new PlanDetailsDTO();
        planDetailsDTO.setPin(plan.getPin());

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

        return planDetailsDTO;
    }

    private ProductionPlanSummaryDTO buildProductionPlanSummaryDTOFromPlan(final Plan plan)
    {

        ProductionPlanSummaryDTO productionPlanSummaryDTO = new ProductionPlanSummaryDTO();
        MicroInjectionDetailsDTO microInjectionDetailsDTO = new MicroInjectionDetailsDTO();
        F1ColonyDetailsDTO f1ColonyDetailsDTO = new F1ColonyDetailsDTO();

        productionPlanSummaryDTO.setMicroInjectionDetailsDTO(microInjectionDetailsDTO);
        productionPlanSummaryDTO.setF1ColonyDetailsDTO(f1ColonyDetailsDTO);
        return productionPlanSummaryDTO;
    }

    private PhenotypePlanSummaryDTO buildPhenotypePlanSummaryDTOFromPlan(final Plan plan)
    {
        PhenotypePlanSummaryDTO phenotypePlanSummaryDTO = new PhenotypePlanSummaryDTO();
        return phenotypePlanSummaryDTO;
    }
}
