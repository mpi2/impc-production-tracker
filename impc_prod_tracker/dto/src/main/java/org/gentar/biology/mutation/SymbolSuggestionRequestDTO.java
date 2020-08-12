package org.gentar.biology.mutation;

import lombok.Data;

@Data
public class SymbolSuggestionRequestDTO
{
    private boolean calculateSymbolAutomatically;
    private String consortiumAbbreviation;
    private boolean excludeConsortiumAbbreviation;
}
