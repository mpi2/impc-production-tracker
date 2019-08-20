package uk.ac.ebi.impc_prod_tracker.web.dto.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class UpdatePlanRequestDTO extends RepresentationModel
{
    private String newPrivacy;

    @JsonProperty("planDetails")
    private PlanDTO planDTO;
}
