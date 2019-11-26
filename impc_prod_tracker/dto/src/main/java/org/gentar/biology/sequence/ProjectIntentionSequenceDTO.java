package org.gentar.biology.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.intention.ProjectIntentionDTO;

@Data
@RequiredArgsConstructor
public class ProjectIntentionSequenceDTO
{

    @JsonProperty("sequenceAttributes")
    private SequenceDTO sequenceDTO;

    @JsonProperty("intention")
    private ProjectIntentionDTO projectIntentionDTO;

    private Integer index;
}
