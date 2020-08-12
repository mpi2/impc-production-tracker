package org.gentar.biology.mutation.symbolConstructor;

import lombok.Data;

@Data
public class SymbolSuggestionRequest
{
    private String ilarCode;
    private String consortiumAbbreviation;
    private boolean excludeConsortiumAbbreviation;
}
