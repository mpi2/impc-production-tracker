package org.gentar.biology.plan.attempt.esCell;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.strain.Strain;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class EsCellAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    private Long imitsMiAttempt;

//    Connection to targ_rep_es_cell
//    private String esCellName;

    private LocalDate miDate;

    private String miExternalRef;

    @Column(columnDefinition = "boolean default false")
    private Boolean experimental;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(cascade = CascadeType.ALL)
    private Strain blast_strain_id;

    private Integer total_blasts_injected;

    private Integer total_transferred;

    private Integer number_surrogates_receiving;

    private Integer total_pups_born;

    private Integer total_female_chimeras;

    private Integer total_male_chimeras;

    //   Calculate by the system: total_female_chimeras + total_male_chimeras
    private Integer total_chimeras;

    private Integer number_of_males_with_0_to_39_percent_chimerism;

    private Integer number_of_males_with_40_to_79_percent_chimerism;

    private Integer number_of_males_with_80_to_99_percent_chimerism;

    private Integer number_of_males_with_100_percent_chimerism;

    @ManyToOne
    private Strain test_cross_strain_id;

    private LocalDate date_chimeras_mated;

    private Integer number_of_chimera_matings_attempted;

    private Integer number_of_chimera_matings_successful;

    private Integer number_of_chimeras_with_glt_from_cct;

    private Integer number_of_chimeras_with_glt_from_genotyping;

    private Integer number_of_chimeras_with_0_to_9_percent_glt;

    private Integer number_of_chimeras_with_10_to_49_percent_glt;

    private Integer number_of_chimeras_with_50_to_99_percent_glt;

    private Integer number_of_chimeras_with_100_percent_glt;

    private Integer total_f1_mice_from_matings;

    private Integer number_of_cct_offspring;

    private LocalDate cassette_transmission_verified;

    //  Set by the system
    @Column(columnDefinition = "boolean default false")
    private Boolean cassette_transmission_verified_auto_complete;

    private Integer number_of_het_offspring;

    private Integer number_of_live_glt_offspring;
}


