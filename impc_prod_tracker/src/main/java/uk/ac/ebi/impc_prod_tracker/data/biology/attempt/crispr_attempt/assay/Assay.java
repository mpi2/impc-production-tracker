package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.assay;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.assay.assay_type.AssayType;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Assay extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "assaySeq", sequenceName = "ASSAY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assaySeq")
    private Long id;

    @ManyToOne(targetEntity = AssayType.class)
    private AssayType assayType;

    private Integer founderNumAssays;

    @Column(name = "num_deletion_g0_mutants")
    private Integer numDeletionG0Mutants;

    @Column(name = "num_g0_where_mutation_detected")
    private Integer numG0WhereMutationDetected;

    @Column(name = "num_hdr_g0_mutants")
    private Integer numHdrG0Mutants;

    @Column(name = "num_hdr_g0_mutants_all_donors_inserted")
    private Integer numHdrG0MutantsAllDonorsInserted;

    @Column(name = "num_nhej_g0_mutants")
    private Integer numNhejG0Mutants;

    @Column(name = "num_hr_g0_mutants")
    private Integer numHrG0Mutants;

    @Column(name = "num_hdr_g0_mutants_subset_donors_inserted")
    private Integer numHdrG0MutantsSubsetDonorsInserted;
}
