/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationAttempt;
import org.gentar.biology.plan.attempt.es_cell.EsCellAttempt;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.plan.status.PlanStatusStamp;
import org.gentar.biology.plan.status.PlanSummaryStatusStamp;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.security.abac.Resource;
import org.gentar.security.abac.ResourcePrivacy;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.breeding.BreedingAttempt;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.flag.PlanFlag;
import org.gentar.biology.plan.protocol.Protocol;
import org.gentar.biology.project.Project;
import org.gentar.biology.status.Status;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Plan extends BaseEntity implements Resource<Plan>, ProcessData
{
    @Id
    @SequenceGenerator(name = "planSeq", sequenceName = "PLAN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planSeq")
    private Long id;

    @Transient
    @Getter
    @Setter
    private ProcessEvent event;

    @EqualsAndHashCode.Include
    private String pin;

    @ToString.Exclude
    @NotNull
    @ManyToOne
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "plan_funder",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "funder_id"))
    private Set<Funder> funders;

    @ManyToOne(targetEntity = WorkUnit.class)
    private WorkUnit workUnit;

    @ManyToOne(targetEntity = WorkGroup.class)
    private WorkGroup workGroup;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;

    @IgnoreForAuditingChanges
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "plan")
    private Set<PlanStatusStamp> planStatusStamps;

    @IgnoreForAuditingChanges
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "plan")
    private Set<PlanSummaryStatusStamp> planSummaryStatusStamps;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status summaryStatus;

    @ManyToOne(targetEntity= PlanType.class)
    private PlanType planType;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToMany()
    @JoinTable(
            name = "plan_flag_relation",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "plan_flag_id"))
    private Set<PlanFlag> planFlags;

    @ManyToMany
    @JoinTable(
            name = "plan_protocol",
            joinColumns = @JoinColumn(name = "plan_id"),
            inverseJoinColumns = @JoinColumn(name = "protocol_id"))
    private Set<Protocol> protocols;

    @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // The @Exclude annotation is not needed;
    // 'onlyExplicitlyIncluded' is set above, so this member would be excluded anyway
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plan")
    private Set<Outcome> outcomes;

    @ToString.Exclude
    // @EqualsAndHashCode.Exclude
    // The @Exclude annotation is not needed;
    // 'onlyExplicitlyIncluded' is set above, so this member would be excluded anyway
    @IgnoreForAuditingChanges
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plan")
    private Set<PlanStartingPoint> planStartingPoints;

    // Copy Constructor
    public Plan(Plan plan)
    {
        this.id = plan.id;
        this.pin = plan.pin;
        this.project = plan.project;
        this.planType = plan.planType;
        this.workUnit = plan.workUnit;
        this.workGroup = plan.workGroup;
        this.funders = new HashSet<>(plan.funders);
        this.comment = plan.comment;
        this.planFlags = new HashSet<>(plan.planFlags);
        this.protocols = new HashSet<>(plan.protocols);
        this.planStatusStamps =
            plan.planStatusStamps == null ? null : new HashSet<>(plan.planStatusStamps);
        this.planSummaryStatusStamps =
            plan.planSummaryStatusStamps == null ? null : new HashSet<>(plan.planSummaryStatusStamps);
        this.outcomes =
            plan.outcomes == null ? null : new HashSet<>(plan.outcomes);
        this.status = plan.status;
        this.summaryStatus = plan.summaryStatus;
        this.attemptType = plan.attemptType;
        this.crisprAttempt = plan.crisprAttempt;
        this.breedingAttempt = plan.breedingAttempt;
        this.esCellAlleleModificationAttempt = plan.esCellAlleleModificationAttempt;
        this.phenotypingAttempt = plan.phenotypingAttempt;
        this.planStartingPoints =
            plan.planStatusStamps == null ? null : new HashSet<>(plan.planStartingPoints);
    }

    @ManyToOne
    private AttemptType attemptType;

    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "plan")
    private CrisprAttempt crisprAttempt;

    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "plan")
    private PhenotypingAttempt phenotypingAttempt;

    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "plan")
    private BreedingAttempt breedingAttempt;

    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "plan")
    private EsCellAlleleModificationAttempt esCellAlleleModificationAttempt;

    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "plan")
    private EsCellAttempt esCellAttempt;

    @Override
    public String toString()
    {
        String planTypeName = planType == null ? "Not defined" : planType.getName();
        String statusName = status == null ? "Not defined" : status.getName();
        return "id:" +id + ", pin:" + pin + ", type: "+ planTypeName + ", status:" +statusName;
    }

    @Override
    public ResourcePrivacy getResourcePrivacy()
    {
        return project.getResourcePrivacy();
    }

    @Override
    @JsonIgnore
    public Resource<Plan> getRestrictedObject()
    {
        return this;
    }

    @Override
    public List<WorkUnit> getRelatedWorkUnits()
    {
        return Arrays.asList(workUnit);
    }

    @Override
    public List<Consortium> getRelatedConsortia()
    {
        return Collections.emptyList();
    }

    @Override
    public ProcessEvent getProcessDataEvent() {
        return this.event;
    }

    @Override
    public void setProcessDataEvent(ProcessEvent processEvent) {
        this.event=processEvent;
    }

    @Override
    public Status getProcessDataStatus() {
        return this.status;
    }

    @Override
    public void setProcessDataStatus(Status status) {
        this.status=status;
    }

}
