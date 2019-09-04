package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;

import java.util.List;

@Data
public class ProjectSummaryDTO extends RepresentationModel
{
    @JsonProperty("projects")
    private ProjectDTO projectDTO;
    @JsonProperty("plans")
    private List<PlanDTO> planDTO;
}
