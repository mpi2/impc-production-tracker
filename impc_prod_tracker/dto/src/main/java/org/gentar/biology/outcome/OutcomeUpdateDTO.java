package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class OutcomeUpdateDTO
{
    @JsonUnwrapped
    private OutcomeCommonDTO outcomeCommonDTO;
}
