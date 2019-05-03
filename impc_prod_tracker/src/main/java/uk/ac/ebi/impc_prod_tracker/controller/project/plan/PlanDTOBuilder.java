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
import uk.ac.ebi.impc_prod_tracker.controller.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.PhenotypePlanSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.ProductionPlanSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.f1_colony.F1ColonyDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.micro_injection.MicroInjectionDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Data
public class PlanDTOBuilder
{
    private PlanMapper planMapper;

    public PlanDTOBuilder(PlanMapper planMapper)
    {
        this.planMapper = planMapper;
    }

    public PlanDTO buildPlanDTOFromPlan(Plan plan)
    {
        PlanDTO planDTO = new PlanDTO();
        PlanDetailsDTO planDetailsDTO = planMapper.convertToPlanDetailsDto(plan);
        planDTO.setPlanDetailsDTO(planDetailsDTO);

        if ("Production".equals(plan.getPlanType().getName()))
        {
            ProductionPlanSummaryDTO productionPlanSummaryDTO =
                planMapper.convertToProductionPlanSummaryDto(plan);
            MicroInjectionDetailsDTO microInjectionDetailsDTO = new MicroInjectionDetailsDTO();
            F1ColonyDetailsDTO f1ColonyDetailsDTO = new F1ColonyDetailsDTO();
            productionPlanSummaryDTO.setMicroInjectionDetailsDTO(microInjectionDetailsDTO);
            productionPlanSummaryDTO.setF1ColonyDetailsDTO(f1ColonyDetailsDTO);

            planDTO.setProductionPlanSummaryDTO(productionPlanSummaryDTO);
        }
        else
        {
            PhenotypePlanSummaryDTO phenotypePlanSummaryDTO =
                planMapper.convertToPhenotypePlanSummaryDto(plan);

            planDTO.setPhenotypePlanSummaryDTO(phenotypePlanSummaryDTO);
        }

        return planDTO;
    }
}
