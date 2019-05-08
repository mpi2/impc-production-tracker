package uk.ac.ebi.impc_prod_tracker.controller.project.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.controller.project.ProjectDetailsDTO;

@Data
public class PlanSummaryDTO
{
    @JsonProperty("projectDetails")
    private ProjectDetailsDTO projectDetailsDTO;
    @JsonProperty("planDetails")
    private PlanDetailsDTO planDetailsDTO;
}
