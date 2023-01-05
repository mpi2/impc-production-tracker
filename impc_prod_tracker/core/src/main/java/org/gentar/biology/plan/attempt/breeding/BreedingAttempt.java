package org.gentar.biology.plan.attempt.breeding;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.strain.Strain;

import jakarta.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class BreedingAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    @IgnoreForAuditingChanges
    @Column(unique = true, insertable = false, updatable = false)
    private Long imitsMouseAlleleMod;

    @Column(name = "number_of_cre_matings_started")
    private Integer numberOfCreMatingsStarted;

    @Column(name = "number_of_cre_matings_successfull")
    private Integer numberOfCareMatingsSuccessful;

    @Column(name = "cre_excesion", columnDefinition = "boolean default false")
    private Boolean creExcesion;

    @Column(name = "tat_cre", columnDefinition = "boolean default false")
    private Boolean tatCre;

    @ManyToOne
    private Strain deleterStrain;

    @ManyToOne
    private BreedingType type;
}
