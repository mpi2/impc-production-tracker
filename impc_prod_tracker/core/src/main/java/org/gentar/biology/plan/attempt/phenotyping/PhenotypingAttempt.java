package org.gentar.biology.plan.attempt.phenotyping;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.strain.Strain;
import org.gentar.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class PhenotypingAttempt extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    @IgnoreForAuditingChanges
    private Plan plan;

    private Long imitsPhenotypeAttempt;
    private Long imitsPhenotypingProduction;
    private Long imitsParentColony;

    private Boolean doNotCountTowardsCompleteness;

    @NotNull
    @Column(unique = true)
    private String phenotypingExternalRef;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Strain strain;

    @ManyToOne(cascade = CascadeType.ALL)
    private WorkUnit cohortWorkUnit;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "phenotypingAttempt")
    private Set<PhenotypingStage> phenotypingStages;

    // Copy Constructor
    public PhenotypingAttempt(PhenotypingAttempt phenotypingAttempt)
    {
        this.id = phenotypingAttempt.id;
        this.plan = phenotypingAttempt.plan;
        this.imitsPhenotypeAttempt = phenotypingAttempt.imitsPhenotypeAttempt;
        this.imitsPhenotypingProduction = phenotypingAttempt.imitsPhenotypingProduction;
        this.imitsParentColony = phenotypingAttempt.imitsParentColony;
        this.doNotCountTowardsCompleteness = phenotypingAttempt.doNotCountTowardsCompleteness;
        this.phenotypingExternalRef = phenotypingAttempt.phenotypingExternalRef;
        this.strain = phenotypingAttempt.strain;
        this.cohortWorkUnit = phenotypingAttempt.cohortWorkUnit;
        this.phenotypingStages =
            phenotypingAttempt.phenotypingStages == null ? null :
                new HashSet<>(phenotypingAttempt.phenotypingStages);
    }

    public void addPhenotypingStage(PhenotypingStage phenotypingStage)
    {
        phenotypingStage.setPhenotypingAttempt(this);
        if (phenotypingStages == null)
        {
            phenotypingStages = new HashSet<>();
        }
        phenotypingStages.add(phenotypingStage);
    }
}
