package org.gentar.biology.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProjectIntentionSequenceDTO
{
    @JsonProperty("sequence")
    private SequenceDTO sequenceDTO;

    private Integer index;
}
