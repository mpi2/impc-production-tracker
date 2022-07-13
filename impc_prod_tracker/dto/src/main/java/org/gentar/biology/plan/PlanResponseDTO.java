package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.util.List;

@Relation(collectionRelation = "plans")
@Data
public class PlanResponseDTO extends RepresentationModel<PlanResponseDTO>
{
    // Public identifier for the plan.
    private String pin;

    @JsonUnwrapped
    private PlanCreationDTO planCreationDTO;

    // Name of the status in the plan.
    private String statusName;

    // Name of the status in the plan.
    @JsonProperty("isAbortionStatus")
    private boolean isAbortionStatus;

    // List of stamps with all the different statuses this plan has had.
    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    // Name of the summary status.
    private String summaryStatusName;

    // List of stamps with all the different summary statuses this plan has had.
    @JsonProperty("summaryStatusDates")
    private List<StatusStampsDTO> summaryStatusStampsDTOS;

    // Available transitions
    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;
}
