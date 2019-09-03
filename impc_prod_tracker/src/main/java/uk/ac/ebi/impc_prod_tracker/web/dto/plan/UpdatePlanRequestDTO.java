package uk.ac.ebi.impc_prod_tracker.web.dto.plan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.phenotype_plan.PhenotypePlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.ProductionPlanDTO;

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
