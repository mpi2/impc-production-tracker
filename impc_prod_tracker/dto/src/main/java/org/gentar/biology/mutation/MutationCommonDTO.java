package org.gentar.biology.mutation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.plan.attempt.crispr.CanonicalTargetedExonDTO;
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
    @JsonIgnore
    @JsonProperty("insertionSequences")
    private List<InsertionSequenceDTO> insertionSequenceDTOS;

    @JsonProperty("mutationCategorizations")
    private List<MutationCategorizationDTO> mutationCategorizationDTOS;
    @JsonIgnore
    @JsonProperty("molecularMutationDeletions")
    private List<MolecularMutationDeletionDTO> molecularMutationDeletionDTOs;
    @JsonIgnore
    @JsonProperty("targetedExons")
    private List<TargetedExonDTO> targetedExonDTOS;
    @JsonIgnore
    @JsonProperty("canonicalTargetedExons")
    private List<CanonicalTargetedExonDTO> canonicalTargetedExonsDTOS;
    @JsonIgnore
    private Boolean isDeletionCoordinatesUpdatedManually;
    @JsonIgnore
    private Boolean isMutationDeletionChecked;


}
