package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class OutcomeUpdateDTO
{
    @JsonUnwrapped
    private OutcomeCommonDTO outcomeCommonDTO;
}
