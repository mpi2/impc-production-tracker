package uk.ac.ebi.impc_prod_tracker.data.experiment.project;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project_priority.ProjectPriority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Getter
@Setter
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

    @Column(unique = true)
    private Long imitsMiPlanId;

    @ManyToOne
    private AssignmentStatus assignmentStatus;

    @ManyToOne
    private ProjectPriority projectPriority;

    @ManyToMany
    @JoinTable(
        name = "project_intention_rel",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "allele_type_id"))
    private Set<AlleleType> projectIntentions;

    @ManyToMany
    @JoinTable(
        name = "project_mouse_gene",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "mouse_gene_id"))
    private Set<IntendedMouseGene> intendedMouseGenes;
}


