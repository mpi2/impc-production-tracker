package org.gentar.biology.plan.attempt.es_cell;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.strain.Strain;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
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

    @IgnoreForAuditingChanges
    @Column(unique = true, insertable = false, updatable = false)
    private Long imitsMiAttempt;

    // Connection to targ_rep_es_cell
    // This stores the id but is not specified as a foreign key
    // The id is converted to the EsCellName in the service and DTO.
    @NotNull
    private Long targRepEsCellId;

    private LocalDate miDate;

    private String miExternalRef;

    @Column(columnDefinition = "boolean default false")
    private Boolean experimental;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(cascade = CascadeType.ALL)
    private Strain blastStrain;

    @Column(name = "total_blasts_injected")
    private Integer totalBlastsInjected;

    @Column(name = "total_transferred")
    private Integer totalTransferred;

    @Column(name = "number_surrogates_receiving")
    private Integer numberSurrogatesReceiving;

    @Column(name = "total_pups_born")
    private Integer totalPupsBorn;

    @Column(name = "total_female_chimeras")
    private Integer totalFemaleChimeras;

    @Column(name = "total_male_chimeras")
    private Integer totalMaleChimeras;

    // Calculate by the system: total_female_chimeras + total_male_chimeras
    @Column(name = "total_chimeras")
    private Integer totalChimeras;

    @Column(name = "number_of_males_with_0_to_39_percent_chimerism")
    private Integer numberOfMalesWith0To39PercentChimerism;

    @Column(name = "number_of_males_with_40_to_79_percent_chimerism")
    private Integer numberOfMalesWith40To79PercentChimerism;

    @Column(name = "number_of_males_with_80_to_99_percent_chimerism")
    private Integer numberOfMalesWith80To99PercentChimerism;

    @Column(name = "number_of_males_with_100_percent_chimerism")
    private Integer numberOfMalesWith100PercentChimerism;

    @ManyToOne(cascade = CascadeType.ALL)
    private Strain testCrossStrain;

    @Column(name = "date_chimeras_mated")
    private LocalDate dateChimerasMated;

    @Column(name = "number_of_chimera_matings_attempted")
    private Integer numberOfChimeraMatingsAttempted;

    @Column(name = "number_of_chimera_matings_successful")
    private Integer numberOfChimeraMatingsSuccessful;

    @Column(name = "number_of_chimeras_with_glt_from_cct")
    private Integer numberOfChimerasWithGltFromCct;

    @Column(name = "number_of_chimeras_with_glt_from_genotyping")
    private Integer numberOfChimerasWithGltFromGenotyping;

    @Column(name = "number_of_chimeras_with_0_to_9_percent_glt")
    private Integer numberOfChimerasWith0To9PercentGlt;

    @Column(name = "number_of_chimeras_with_10_to_49_percent_glt")
    private Integer numberOfChimerasWith10To49PercentGlt;

    @Column(name = "number_of_chimeras_with_50_to_99_percent_glt")
    private Integer numberOfChimerasWith50To99PercentGlt;

    @Column(name = "number_of_chimeras_with_100_percent_glt")
    private Integer numberOfChimerasWith100PercentGlt;

    @Column(name = "total_f1_mice_from_matings")
    private Integer totalF1MiceFromMatings;

    @Column(name = "number_of_cct_offspring")
    private Integer numberOfCctOffspring;

    // Set by the system
    @Column(name = "cassette_transmission_verified")
    private LocalDate cassetteTransmissionVerified;

    // Set by the system
    @Column(name = "cassette_transmission_verified_auto_complete", columnDefinition = "boolean default false")
    private Boolean cassetteTransmissionVerifiedAutoComplete;

    @Column(name = "number_of_het_offspring")
    private Integer numberOfHetOffspring;

    @Column(name = "number_of_live_glt_offspring")
    private Integer numberOfLiveGltOffspring;
}


