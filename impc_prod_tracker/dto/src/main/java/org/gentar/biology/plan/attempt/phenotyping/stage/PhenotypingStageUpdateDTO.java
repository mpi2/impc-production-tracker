package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class PhenotypingStageUpdateDTO
{
    @JsonUnwrapped
    private PhenotypingStageCommonDTO phenotypingStageCommonDTO;
}
