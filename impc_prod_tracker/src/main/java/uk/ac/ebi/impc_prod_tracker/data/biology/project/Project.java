package uk.ac.ebi.impc_prod_tracker.data.biology.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.Resource;
import uk.ac.ebi.impc_prod_tracker.conf.security.ResourcePrivacy;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status_stamp.AssignmentStatusStamp;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_gene.ProjectGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_location.ProjectLocation;
import uk.ac.ebi.impc_prod_tracker.data.biology.species.Species;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Project extends BaseEntity implements Resource<Project>
{
    @Id
    @SequenceGenerator(name = "projectSeq", sequenceName = "PROJECT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectSeq")
    private Long id;

    @Column(unique = true)
    @NotNull
    private String tpn;

//    @PostPersist
//    public void postPersist()
//    {
//        tpn = "TPN:" + String.format("%0" + 9 + "d", id);
//    }

    @Column(unique = true)
    private Long imitsMiPlanId;

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private AssignmentStatus assignmentStatus;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "project_id")
    private Set<AssignmentStatusStamp> assignmentStatusStamps;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "project_id")
    private Set<Plan> plans;

    private Boolean withdrawn;

    private Boolean recovery;

//    @NotNull
    private Boolean isActive;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(columnDefinition = "TEXT")
    private String projectExternalRef;




    //    @NotNull
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
                throw new OperationFailedException("Invalid privacy");
        }
        return resourcePrivacy;
    }

    @JsonIgnore
    public Project getRestrictedObject()
    {
        /* TODO
        Plan plan = new Plan();

        plan.setPrivacy(this.privacy);
        plan.setPin(this.pin);
        plan.setPlanType(this.planType);
        Project restrictedProject = new Project();
        restrictedProject.setTpn(this.project.getTpn());
        plan.setProject(restrictedProject);

        return plan;
        */
        return null;
    }


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "project")
    private Set<ProjectGene> genes;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "project_id")
    private Set<ProjectLocation> locations;


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "projects")
    private Set<Consortium> consortia;


    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "project_species",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "species_id"))
    private Set<Species> species;
}


