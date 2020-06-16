package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class OutcomeResponseDTO extends RepresentationModel<OutcomeResponseDTO>
{
    private String pin;

    private String tpo;

    private String outcomeTypeName;

    @JsonUnwrapped
    private OutcomeCommonDTO outcomeCommonDTO;
}
