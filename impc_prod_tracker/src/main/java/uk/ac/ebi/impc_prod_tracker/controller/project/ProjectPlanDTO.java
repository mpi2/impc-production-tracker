package uk.ac.ebi.impc_prod_tracker.controller.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDTO;

@Data
//TODO check if obsolete
public class ProjectPlanDTO
{
    @JsonProperty("projectDetails")
    private ProjectDetailsDTO projectDetailsDTO;
    @JsonProperty("planInformation")
    private PlanDTO planDTO;
}
