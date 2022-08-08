package org.gentar.biology.plan.attempt.phenotyping.stage;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.status.Status;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
@Table(
        name="PHENOTYPING_STAGE",
        uniqueConstraints={@UniqueConstraint(columnNames={"plan_id", "phenotyping_stage_type_id"})})
public class PhenotypingStage extends BaseEntity implements ProcessData
{
    @Id
    @SequenceGenerator(name = "phenotypingStageSeq", sequenceName = "PHENOTYPING_STAGE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phenotypingStageSeq")
    private Long id;

    @EqualsAndHashCode.Include
    private String psn;

    @Transient
    private ProcessEvent event;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private PhenotypingAttempt phenotypingAttempt;

    @NotNull
    @ManyToOne(targetEntity= PhenotypingStageType.class)
    private PhenotypingStageType phenotypingStageType;

    private LocalDate phenotypingExperimentsStarted;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "phenotypingStage", orphanRemoval=true)
    private Set<TissueDistribution> tissueDistributions;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;

    private LocalDate initialDataReleaseDate;

    @IgnoreForAuditingChanges
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "phenotypingStage")
    private Set<PhenotypingStageStatusStamp> phenotypingStageStatusStamps;

    public PhenotypingStage(PhenotypingStage phenotypingStage) {
        this.id = phenotypingStage.id;
        this.psn = phenotypingStage.psn;
        this.status = phenotypingStage.status;
        this.phenotypingAttempt = phenotypingStage.phenotypingAttempt;
        this.phenotypingExperimentsStarted = phenotypingStage.phenotypingExperimentsStarted;
        this.initialDataReleaseDate = phenotypingStage.initialDataReleaseDate;
        this.phenotypingStageType = phenotypingStage.phenotypingStageType;
        if (phenotypingStage.tissueDistributions != null)
        {
            this.tissueDistributions = new HashSet<>(phenotypingStage.tissueDistributions);
        }
        if (phenotypingStage.phenotypingStageStatusStamps != null)
        {
            this.phenotypingStageStatusStamps = new HashSet<>(phenotypingStage.phenotypingStageStatusStamps);
        }
    }

    public void addTissueDistribution(TissueDistribution tissueDistribution)
    {
        if (tissueDistributions == null)
        {
            tissueDistributions = new HashSet<>();
        }
        tissueDistributions.add(tissueDistribution);
        tissueDistribution.setPhenotypingStage(this);
    }

    @Override
    public String toString()
    {
        String phenotypingStageTypeName =
            phenotypingStageType == null ? "" : phenotypingStageType.getName();
        String statusName = status == null ? "" : status.getName();
        List<String> values = new ArrayList<>();
        values.add("id=" + id);
        values.add("psn=" + psn);
        values.add("phenotypingStageType=" + phenotypingStageTypeName);
        values.add("phenotypingExperimentsStarted=" + phenotypingExperimentsStarted);
        values.add("statusName=" + statusName);
        values.add("initialDataReleaseDate=" + initialDataReleaseDate);
        return String.join(",", values);
    }

    @Override
    public void setProcessDataStatus(Status status) {
        this.status=status;
    }
}
