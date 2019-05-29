package uk.ac.ebi.impc_prod_tracker.data.experiment.project;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project_mouse_gene.ProjectMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project_priority.ProjectPriority;
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

    @Column(unique = true)
    private Long imitsMiPlanId;

    @ManyToOne
    private AssignmentStatus assignmentStatus;

    @ManyToOne
    private ProjectPriority projectPriority;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectMouseGene> projectMouseGenes = new HashSet<>();
}


