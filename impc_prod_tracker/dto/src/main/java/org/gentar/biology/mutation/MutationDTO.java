package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@RequiredArgsConstructor
public class MutationDTO
{
    private Long id;
    private String mgiAlleleAccessionId;
    private String mgiAlleleSymbolSuperScript;
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
}
