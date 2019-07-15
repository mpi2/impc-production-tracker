package uk.ac.ebi.impc_prod_tracker.data.experiment.project;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Project extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "projectSeq", sequenceName = "PROJECT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectSeq")
    private Long id;

    @Column(unique = true)
    @NotNull
    private String tpn;

    @PostPersist
    public void postPersist()
    {
        tpn = "TPN:" + String.format("%0" + 9 + "d", id);
    }

    @Column(unique = true)
    private Long imitsMiPlanId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "project_id")
    private Set<Plan> plans = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private AssignmentStatus assignmentStatus;

//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @ManyToOne
//    private ProjectPriority projectPriority;

//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @ManyToMany
//    @JoinTable(
//        name = "project_intention_rel",
//        joinColumns = @JoinColumn(name = "project_id"),
//        inverseJoinColumns = @JoinColumn(name = "allele_type_id"))
//    private Set<AlleleType> projectIntentions;
//
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @ManyToMany
//    @JoinTable(
//        name = "project_mouse_gene",
//        joinColumns = @JoinColumn(name = "project_id"),
//        inverseJoinColumns = @JoinColumn(name = "mouse_gene_id"))
//    private Set<IntendedMouseGene> intendedMouseGenes;
}


