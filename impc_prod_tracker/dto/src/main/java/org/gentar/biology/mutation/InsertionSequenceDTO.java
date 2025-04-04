package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InsertionSequenceDTO
{
    private Long id;

    @JsonProperty("sequence")
    private String sequence;

}
