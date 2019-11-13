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
package uk.ac.ebi.impc_prod_tracker.data.biology.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.SystemOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.Resource;
import uk.ac.ebi.impc_prod_tracker.conf.security.ResourcePrivacy;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status_stamp.AssignmentStatusStamp;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_consortium.ProjectConsortium;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.biology.species.Species;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
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
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Set<AssignmentStatusStamp> assignmentStatusStamps;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "project_id")
    private Set<Plan> plans;

    private Boolean withdrawn;

    private Boolean recovery;

    private Boolean isActive;

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
        restrictedProject.setIsActive(isActive);
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
            plans.forEach(x -> relatedWorkUnites.add(x.getWorkUnit()));
        }
        return relatedWorkUnites;
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
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "project", orphanRemoval=true)
    private Set<ProjectConsortium> projectConsortia;
}
