package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.gene.GeneDTO;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MutationDTO
{
    private Long id;
    private String mgiAlleleAccessionId;
    private String mgiAlleleSymbol;
    private Boolean mgiAlleleSymbolRequiresConstruction;
    private String geneticMutationTypeName;
    private String molecularMutationTypeName;
    private Integer genBankFileId;
    private Integer vcfFileIndex;
    private Integer bamFileIndex;
    private String description;
    private String autoDescription;
    private Boolean alleleConfirmed;
    private String alleleSymbolSuperscriptTemplate;

    @JsonProperty("mutationQcResults")
    private List<MutationQCResultDTO> mutationQCResultDTOs;
    @JsonProperty("genes")
    private List<GeneDTO> geneDTOS;
    @JsonProperty("mutationCategorizations")
    private List<MutationCategorizationDTO> mutationCategorizationDTOS;
}
