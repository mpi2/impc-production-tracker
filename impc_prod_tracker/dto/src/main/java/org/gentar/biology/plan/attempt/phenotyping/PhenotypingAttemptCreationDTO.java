package org.gentar.biology.plan.attempt.phenotyping;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageCreationDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PhenotypingAttemptCreationDTO
{
    @JsonUnwrapped
    private PhenotypingAttemptCommonDTO phenotypingAttemptCommonDTO;

    @JsonProperty("phenotypingStages")
    private List<PhenotypingStageCreationDTO> phenotypingStageCreationDTOS;
}
