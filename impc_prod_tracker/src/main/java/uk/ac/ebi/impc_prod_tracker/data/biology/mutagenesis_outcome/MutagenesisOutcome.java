package uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_outcome;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_attempt.MutagenesisAttempt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MutagenesisOutcome extends BaseEntity
{
    @Id
    private Long id;

    @OneToOne
    @JoinColumn
    @MapsId
    private MutagenesisAttempt mutagenesisAttempt;

    @Column(name = "no_g0_where_mutation_detected")
    private Integer noG0WhereMutationDetected;

    @Column(name = "no_nhej_g0_mutants")
    private Integer noNhejG0Mutants;

    @Column(name = "no_deletion_g0_mutants")
    private Integer noDeletionG0Mutants;

    @Column(name = "no_hr_g0_mutants")
    private Integer noHrG0Mutants;

    @Column(name = "no_hdr_g0_mutants")
    private Integer noHdrG0Mutants;

    @Column(name = "no_hdr_g0_mutants_all_donors_inserted")
    private Integer noHdrG0MutantsAllDonorsInserted;

    @Column(name = "no_hdr_g0_mutants_subset_donors_inserted")
    private Integer noHdrG0MutantsSubsetDonorsInserted;
}
