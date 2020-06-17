package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import org.gentar.common.state_machine.StatusTransitionDTO;

@Data
public class PhenotypingStageUpdateDTO
{
    private Long id;
    private String psn;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;

    @JsonUnwrapped
    private PhenotypingStageCommonDTO phenotypingStageCommonDTO;
}
