package org.gentar.biology.plan.attempt.crispr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AssayDTO
{
    private Long id;
    private String typeName;

    @JsonProperty("embryoTransferDay")
    private String embryoTransferDay;


    @JsonProperty("totalTransferred")
    private Integer totalTransferred;

    private Integer numFounderPups;
    private Integer numFounderSelectedForBreeding;
    private Integer founderNumAssays;
    private Integer numDeletionG0Mutants;
    private Integer numG0WhereMutationDetected;
    private Integer numHdrG0Mutants;
    private Integer numHdrG0MutantsAllDonorsInserted;
    private Integer numHdrG0MutantsSubsetDonorsInserted;
    private Integer numHrG0Mutants;
    private Integer numNhejG0Mutants;
}
