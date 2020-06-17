package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;

import java.util.List;

@Data
public class PhenotypingStageResponseDTO
{
    private Long id;
    private String pin;
    private String psn;
    private String statusName;
    private String phenotypingTypeName;
    private String phenotypingExternalRef;

    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonProperty("statusTransition")
    private StatusTransitionDTO statusTransitionDTO;

    @JsonUnwrapped
    private PhenotypingStageCommonDTO phenotypingStageCommonDTO;
}
