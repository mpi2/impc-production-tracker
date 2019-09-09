package uk.ac.ebi.impc_prod_tracker.data.biology.project_gene;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.molecular_mutation_type.MolecularMutationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProjectGene extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "projectGeneSeq", sequenceName = "PROJECT_GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectGeneSeq")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne(targetEntity = Gene.class)
    private Gene gene;

    @ManyToOne(targetEntity= MolecularMutationType.class)
    private MolecularMutationType molecularMutationType;

    @ManyToOne(targetEntity = AlleleType.class)
    private AlleleType alleleType;
}
