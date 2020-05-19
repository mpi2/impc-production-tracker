package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class PlanResponseDTO extends RepresentationModel
{
    // Public identifier for the plan.
    private String pin;

    // Public identifier of the project for the plan.
    private String tpn;

    // Name of the status in the plan.
    private String statusName;

    // Name of the summary status.
    private String summaryStatusName;

    @JsonUnwrapped
    private PlanCreationDTO planCreationDTO;

    // List of stamps with all the different statuses this plan has had.
    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    // List of stamps with all the different summary statuses this plan has had.
    @JsonProperty("summaryStatusDates")
    private List<StatusStampsDTO> summaryStatusStampsDTOS;

    // Available transitions
    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;
}
