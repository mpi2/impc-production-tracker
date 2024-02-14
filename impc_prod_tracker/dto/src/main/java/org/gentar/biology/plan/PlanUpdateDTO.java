package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.common.state_machine.StatusTransitionDTO;

/**
 * Fields that are editable in a plan and that are expected to be sent by the user in the payload
 * for an update operation (or creation, with other required fields).
 */
@Data
public class PlanUpdateDTO
{
    @JsonIgnore
    // Internal id useful keep the id accessible in all the plan DTOS.
    private Long id;

    // Public identifier for the plan.
    private String pin;

    // Public identifier of the project for the plan.
    private String tpn;

    @JsonUnwrapped
    private PlanBasicDataDTO planBasicDataDTO;

    // Contains the information of the action the user wants to execute in the state machine of the
    // plan.
    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;
}
