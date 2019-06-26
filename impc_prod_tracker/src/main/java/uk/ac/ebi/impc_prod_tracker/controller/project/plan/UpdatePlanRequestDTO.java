package uk.ac.ebi.impc_prod_tracker.controller.project.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.PhenotypePlanDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.ProductionPlanDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.AttemptDTO;

@Data
public class UpdatePlanRequestDTO
{
    private String newPrivacy;

    @JsonProperty("planDetails")
    private PlanDetailsDTO planDetailsDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("productionPlan")
    private ProductionPlanDTO productionPlanDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("phenotypePlan")
    private PhenotypePlanDTO phenotypePlanDTO;
}
