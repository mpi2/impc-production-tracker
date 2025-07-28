package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class InsertionSequenceDTO
{
    private Long id;

    private String ins;

    @JsonProperty("sequence")
    private String sequence;

    private String location;

    @JsonProperty("insertionCanonicalTargetedExons")
    private List<InsertionCanonicalTargetedExonDTO> insertionCanonicalTargetedExonsDTO;

    @JsonProperty("insertionTargetedExons")
    private List<InsertionTargetedExonDTO> insertionTargetedExonsDTO;

}
