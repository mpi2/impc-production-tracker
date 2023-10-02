package org.gentar.report.dto.crispr_product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assay {

    public Integer numFounderPups;
    public Integer numFounderSelectedForBreeding;
    public Integer founderNumAssays;
    public Integer numDeletionG0Mutants;
    public Integer numG0WhereMutationDetected;
    public Integer numHdrG0Mutants;
    public Integer numHdrG0MutantsAllDonorsInserted;
    public Integer numHdrG0MutantsSubsetDonorsInserted;
    public Integer numHrG0Mutants;
    public Integer numNhejG0Mutants;
    public String assayType;
}
