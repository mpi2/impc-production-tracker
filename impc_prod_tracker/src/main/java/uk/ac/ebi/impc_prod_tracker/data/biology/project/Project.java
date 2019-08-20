package uk.ac.ebi.impc_prod_tracker.data.biology.project;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_gene.ProjectGene;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

//    @PostPersist
//    public void postPersist()
//    {
//        tpn = "TPN:" + String.format("%0" + 9 + "d", id);
//    }

    @Column(unique = true)
    private Long imitsMiPlanId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    private AssignmentStatus assignmentStatus;

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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "project")
    private Set<ProjectGene> projectGenes;
}


