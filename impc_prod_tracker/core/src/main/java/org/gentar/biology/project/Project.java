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
package org.gentar.biology.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.summary_status.ProjectSummaryStatusStamp;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.security.abac.Resource;
import org.gentar.BaseEntity;
import org.gentar.biology.project.assignment.AssignmentStatus;
import org.gentar.biology.project.assignment.AssignmentStatusStamp;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.species.Species;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.security.abac.ResourcePrivacy;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Project extends BaseEntity implements Resource<Project>
{
    transient private Boolean isObjectRestricted = null;

    // Copy Constructor
    public Project(Project project)
    {
        this.id = project.id;
        this.tpn = project.tpn;
        this.imitsMiPlan = project.imitsMiPlan;
        this.assignmentStatus = project.assignmentStatus;
        this.summaryStatus = project.summaryStatus;
        this.assignmentStatusStamps =
            project.assignmentStatusStamps == null ? null : new HashSet<>(project.assignmentStatusStamps);
        this.summaryStatusStamps =
            project.summaryStatusStamps == null ? null : new HashSet<>(project.summaryStatusStamps);
        this.plans = project.plans == null ? null : new HashSet<>(project.plans);
        this.reactivationDate = project.reactivationDate;
        this.recovery = project.recovery;
        this.comment = project.comment;
        this.privacy = project.privacy;
        this.projectIntentions =
            project.projectIntentions == null ? null : new ArrayList<>(project.projectIntentions);
        this.species = project.species == null ? null : new HashSet<>(project.species);
        this.projectConsortia =
            project.projectConsortia == null ? null : new HashSet<>(project.projectConsortia);
    }

    @Id
    @SequenceGenerator(name = "projectSeq", sequenceName = "PROJECT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectSeq")
    private Long id;

    private String tpn;

    @Column(unique = true)
    private Long imitsMiPlan;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private AssignmentStatus assignmentStatus;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(targetEntity= Status.class)
    private Status summaryStatus;

    @IgnoreForAuditingChanges
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "project", orphanRemoval=true, fetch=FetchType.EAGER)
    private Set<AssignmentStatusStamp> assignmentStatusStamps;

    @IgnoreForAuditingChanges
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade= CascadeType.ALL, mappedBy = "project", orphanRemoval=true, fetch=FetchType.EAGER)
    private Set<ProjectSummaryStatusStamp> summaryStatusStamps;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "project_id")
    private Set<Plan> plans;

    private LocalDateTime reactivationDate;

    private Boolean recovery;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(targetEntity= Privacy.class)
    private Privacy privacy;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project")
    private List<ProjectIntention> projectIntentions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "project_species",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "species_id"))
    private Set<Species> species;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project", orphanRemoval=true)
    private Set<ProjectConsortium> projectConsortia;

    @Override
    @JsonIgnore
    public ResourcePrivacy getResourcePrivacy()
    {
        ResourcePrivacy resourcePrivacy;
        switch (privacy.getName().toLowerCase())
        {
            case "public":
                resourcePrivacy = ResourcePrivacy.PUBLIC;
                break;
            case "protected":
                resourcePrivacy = ResourcePrivacy.PROTECTED;
                break;
            case "restricted":
                resourcePrivacy = ResourcePrivacy.RESTRICTED;
                break;
            default:
                String detailedError = "Privacy: " + privacy.getName() + " in project " + toString();
                throw new SystemOperationFailedException("Invalid privacy", detailedError);
        }
        return resourcePrivacy;
    }

    @Override
    @JsonIgnore
    public Project getRestrictedObject()
    {
        Project restrictedProject = new Project();
        restrictedProject.setId(id);
        restrictedProject.setTpn(tpn);
        restrictedProject.setPrivacy(privacy);
        restrictedProject.setReactivationDate(reactivationDate);
        restrictedProject.setComment(comment);
        restrictedProject.setRecovery(recovery);
        restrictedProject.setProjectIntentions(projectIntentions);
        restrictedProject.setIsObjectRestricted(true);
        return restrictedProject;
    }

    @Override
    public List<WorkUnit> getRelatedWorkUnits()
    {
        List<WorkUnit> relatedWorkUnites = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(x -> {
                WorkUnit workUnit = x.getWorkUnit();
                if (workUnit != null)
                {
                    relatedWorkUnites.add(workUnit);
                }
            } );
        }
        return relatedWorkUnites;
    }

    public List<WorkUnit> getRelatedProductionPlanWorkUnits()
    {
        List<WorkUnit> relatedWorkUnites = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(x -> {
                WorkUnit workUnit = x.getWorkUnit();
                if (workUnit != null &&
                    x.getPlanType().getName().equals(PlanTypeName.PRODUCTION.getLabel()))
                {
                    relatedWorkUnites.add(workUnit);
                }
            } );
        }
        return relatedWorkUnites;
    }

    public List<WorkGroup> getRelatedWorkGroups()
    {
        List<WorkGroup> relatedWorkGroups = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(x -> {
                WorkGroup workGroup = x.getWorkGroup();
                if (workGroup != null)
                {
                    relatedWorkGroups.add(workGroup);
                }
            });
        }
        return relatedWorkGroups;
    }

    @Override
    public List<Consortium> getRelatedConsortia()
    {
        List<Consortium> relatedConsortia = new ArrayList<>();
        if (projectConsortia != null)
        {
            projectConsortia.forEach(x -> relatedConsortia.add(x.getConsortium()));
        }
        return relatedConsortia;
    }

    public List<Colony> getRelatedColonies()
    {
        List<Colony> relatedColonies = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(x -> {
                Set<Outcome> outcomes = x.getOutcomes();
                if (outcomes != null)
                {
                    outcomes.forEach(o -> {
                        Colony colony = o.getColony();
                        relatedColonies.add(colony);
                    });
                }
            });
        }
        return relatedColonies;
    }

    public void addPlan(Plan plan)
    {
        if (this.plans == null)
        {
            this.plans = new HashSet<>();
        }
        this.plans.add(plan);
    }

    public String toString()
    {
        String assignmentStatusName =
            assignmentStatus == null ? "Not defined" : assignmentStatus.getName();
        return "tpn=" + tpn + ",assignmentStatus=" + assignmentStatusName;
    }

}
