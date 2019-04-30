package uk.ac.ebi.impc_prod_tracker.data.experiment.project;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assigment_status.AssigmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project_priority.ProjectPriority;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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

    private String tpn;

    @ManyToOne
    private AssigmentStatus assigmentStatus;

    @ManyToOne
    private ProjectPriority projectPriority;

    @ManyToMany
    @JoinTable(
        name = "project_intention_rel",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "allele_type_id"))
    @JsonManagedReference
    private Set<AlleleType> projectIntentions;

    @ManyToMany
    @JoinTable(
        name = "project_mouse_gene",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "mouse_gene_id"))
    @JsonManagedReference
    private Set<IntendedMouseGene> intendedMouseGenes;
}


