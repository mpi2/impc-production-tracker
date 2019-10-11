package uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AssayDTO
{
    private Long id;
    private String typeName;
    private Integer founderNumAssays;
    private Integer numDeletionG0Mutants;
    private Integer numG0WhereMutationDetected;
    private Integer numHdrG0Mutants;
    private Integer numHdrG0MutantsAllDonorsInserted;
    private Integer numHdrG0MutantsSubsetDonorsInserted;
    private Integer numHrG0Mutants;
    private Integer numNhejG0Mutants;
}
