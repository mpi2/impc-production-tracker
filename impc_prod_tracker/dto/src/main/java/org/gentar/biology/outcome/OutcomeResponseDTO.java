package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class OutcomeResponseDTO
{
    private Long id;

    private String pin;

    private String tpo;

    private String outcomeTypeName;

    @JsonUnwrapped
    private OutcomeCommonDTO outcomeCommonDTO;
}
