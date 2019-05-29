package uk.ac.ebi.impc_prod_tracker.data.experiment.project_mouse_gene;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene.IntendedMouseGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene.MouseGene;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProjectMouseGene extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "projectMouseGeneSeq", sequenceName = "PROJECT_MOUSE_GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectMouseGeneSeq")
    private Long id;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Project project;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private IntendedMouseGene mouseGene;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private AlleleType alleleType;
}
