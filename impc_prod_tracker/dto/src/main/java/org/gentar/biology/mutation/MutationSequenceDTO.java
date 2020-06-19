package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.sequence.SequenceDTO;

@Data
public class MutationSequenceDTO
{
    private Long id;

    @JsonProperty("sequence")
    private SequenceDTO sequenceDTO;

    private Integer index;
}
