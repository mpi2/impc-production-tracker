package org.gentar.biology.plan.attempt.crispr.assay;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;

import jakarta.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Assay extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "attempt_id")
    @MapsId
    private CrisprAttempt crisprAttempt;

    @ManyToOne(targetEntity = AssayType.class)
    private AssayType assayType;

    private Integer numFounderPups;

    private Integer numFounderSelectedForBreeding;

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
