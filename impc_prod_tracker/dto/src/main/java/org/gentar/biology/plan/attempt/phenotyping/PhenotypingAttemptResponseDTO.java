package org.gentar.biology.plan.attempt.phenotyping;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@RequiredArgsConstructor
public class PhenotypingAttemptResponseDTO extends RepresentationModel<PhenotypingAttemptResponseDTO>
{
    @JsonUnwrapped
    private PhenotypingAttemptCommonDTO phenotypingAttemptCommonDTO;
}
