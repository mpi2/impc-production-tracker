package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.plan.attempt.crispr.TargetedExonDTO;

import java.util.List;

@Data
public class MutationCommonDTO
{
    private String symbol;
    private String description;
    private String qcNote;
    private Boolean mgiAlleleSymbolRequiresConstruction;
    private String geneticMutationTypeName;
    private String molecularMutationTypeName;

    @JsonIgnore
    private Boolean alleleConfirmed;

    @JsonProperty("mutationQcResults")
    private List<MutationQCResultDTO> mutationQCResultDTOs;

    @JsonProperty("mutationSequences")
    private List<MutationSequenceDTO> mutationSequenceDTOS;

    @JsonProperty("mutationCategorizations")
    private List<MutationCategorizationDTO> mutationCategorizationDTOS;

    @JsonProperty("molecularMutationDeletions")
    private List<MolecularMutationDeletionDTO> molecularMutationDeletionDTOs;

    @JsonProperty("targetedExons")
    private List<TargetedExonDTO> targetedExonDTOS;

    private Boolean isManualMutationDeletion;

    private Boolean isMutationDeletionChecked;

}
