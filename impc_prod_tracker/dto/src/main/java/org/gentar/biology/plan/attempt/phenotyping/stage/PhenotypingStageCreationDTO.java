package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class PhenotypingStageCreationDTO
{
    private String phenotypingTypeName;

    @JsonUnwrapped
    private PhenotypingStageCommonDTO phenotypingStageCommonDTO;
}
