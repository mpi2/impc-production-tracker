package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.gentar.biology.gene.GeneUpdateDTO;
import java.util.List;

@Data
public class MutationUpdateDTO
{
    @JsonUnwrapped
    private MutationCommonDTO mutationCommonDTO;

    @JsonProperty("genes")
    private List<GeneUpdateDTO> geneUpdateDTOS;

    @JsonProperty("symbolSuggestionRequest")
    private SymbolSuggestionRequestDTO symbolSuggestionRequestDTO;
}
