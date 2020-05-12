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
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
@Table(
    name="PHENOTYPING_STAGE",
    uniqueConstraints=@UniqueConstraint(columnNames={"plan_id", "phenotyping_stage_type_id"}))
public class PhenotypingStage extends BaseEntity implements ProcessData
{
    @Id
    @SequenceGenerator(name = "phenotypingStageSeq", sequenceName = "PHENOTYPING_STAGE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phenotypingStageSeq")
    private Long id;

    @Transient
    private ProcessEvent event;

    @NotNull
    @EqualsAndHashCode.Exclude
    @ManyToOne(targetEntity = PhenotypingAttempt.class)
    @JoinColumn(name = "plan_id")
    private PhenotypingAttempt phenotypingAttempt;

    @NotNull
    @ManyToOne(targetEntity= PhenotypingStageType.class)
    private PhenotypingStageType phenotypingStageType;

    private LocalDate phenotypingExperimentsStarted;

    private Boolean doNotCountTowardsCompleteness;

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

    public String toString()
    {
        List<String> values = new ArrayList<>();
        values.add("id=" + id);
        values.add("phenotypingStageType=" + phenotypingStageType.getName());
        values.add("phenotypingExperimentsStarted=" + phenotypingExperimentsStarted);
        values.add("statusName=" + status.getName());
        values.add("initialDataReleaseDate=" + initialDataReleaseDate);
        return String.join(",", values);
    }
}
