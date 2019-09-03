package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDetailsDTO;

import java.util.List;

@Data
public class ProjectSummaryDTO
{
    @JsonProperty("projectDetails")
    private ProjectDetailsDTO projectDetailsDTO;
    @JsonProperty("planDetails")
    private List<PlanDetailsDTO> planDetailsDTO;
}
