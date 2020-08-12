package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import java.util.List;

@Data
public class MutationUpdateDTO
{
    @JsonUnwrapped
    private MutationCommonDTO mutationCommonDTO;

    @JsonProperty("geneSymbolsOrAccessionIds")
    private List<String> symbolOrAccessionIds;

    @JsonProperty("symbolSuggestionRequest")
    private SymbolSuggestionRequestDTO symbolSuggestionRequestDTO;
}
