package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class MutationResponseDTO
{
    private Long id;
    private String mgiAlleleId;
    private String mgiAlleleSymbol;
    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;
    private String description;
    private String autoDescription;
    @JsonUnwrapped
    private MutationCommonDTO mutationCommonDTO;


}
