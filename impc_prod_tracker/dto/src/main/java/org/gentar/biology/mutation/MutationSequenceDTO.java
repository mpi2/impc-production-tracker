package org.gentar.biology.mutation;

import lombok.Data;
import org.gentar.biology.sequence.SequenceDTO;

@Data
public class MutationSequenceDTO
{
    private Long id;

    private SequenceDTO sequenceDTO;

    private Integer index;
}
