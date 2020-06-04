package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class OutcomeCreationDTO
{
    private String outcomeTypeName;

    @JsonUnwrapped
    private OutcomeCommonDTO outcomeCommonDTO;
}
