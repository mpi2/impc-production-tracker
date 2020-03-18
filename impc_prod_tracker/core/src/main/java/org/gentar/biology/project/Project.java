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
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Project extends BaseEntity implements Resource<Project>
{
    transient private Boolean isObjectRestricted = null;

    @Id
    @SequenceGenerator(name = "projectSeq", sequenceName = "PROJECT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectSeq")
    private Long id;

    private String tpn;

    @Column(unique = true)
    private Long imitsMiPlanId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private AssignmentStatus assignmentStatus;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(targetEntity= Status.class)
    private Status summaryStatus;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Set<AssignmentStatusStamp> assignmentStatusStamps;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Set<Plan> plans;

    private Boolean withdrawn;

    private Boolean recovery;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(columnDefinition = "TEXT")
    private String projectExternalRef;

    @ManyToOne(targetEntity= Privacy.class)
    private Privacy privacy;

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

    @JsonIgnore
    public Project getRestrictedObject()
    {
        Project restrictedProject = new Project();
        restrictedProject.setId(id);
        restrictedProject.setTpn(tpn);
        restrictedProject.setPrivacy(privacy);
        restrictedProject.setWithdrawn(withdrawn);
        restrictedProject.setComment(comment);
        restrictedProject.setRecovery(recovery);
        restrictedProject.setProjectExternalRef(projectExternalRef);
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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project")
    private List<ProjectIntention> projectIntentions;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "project_species",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "species_id"))
    private Set<Species> species;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project", orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<ProjectConsortium> projectConsortia;
}
