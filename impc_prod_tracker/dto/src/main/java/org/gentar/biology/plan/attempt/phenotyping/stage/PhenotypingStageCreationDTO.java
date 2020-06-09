package org.gentar.biology.plan.attempt.phenotyping.stage;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PhenotypingStageCreationDTO
{
    private String phenotypingTypeName;

    @JsonUnwrapped
    private PhenotypingStageCommonDTO phenotypingStageCommonDTO;
}
