package uk.ac.ebi.impc_prod_tracker.data.biology.project_gene;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_categorization.AlleleCategorization;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.molecular_mutation_type.MolecularMutationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

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

    @NotNull
    @ManyToOne(targetEntity= MolecularMutationType.class)
    private MolecularMutationType molecularMutationType;

    @ManyToOne(targetEntity = AlleleType.class)
    private AlleleType alleleType;


    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "project_gene_allele_categorization",
            joinColumns = @JoinColumn(name = "project_gene_id"),
            inverseJoinColumns = @JoinColumn(name = "allele_categorization_id"))
    private Set<AlleleCategorization> alleleCategorizations;
}
