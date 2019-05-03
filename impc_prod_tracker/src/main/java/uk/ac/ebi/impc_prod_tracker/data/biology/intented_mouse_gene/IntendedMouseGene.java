package uk.ac.ebi.impc_prod_tracker.data.biology.intented_mouse_gene;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_flag.GeneFlag;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class IntendedMouseGene extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "intendedMouseGeneSeq", sequenceName = "INTENDED_MOUSE_GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "intendedMouseGeneSeq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    private String symbol;

    private String mgiId;

    private String type;

    private String genomeBuild;

    private Long entrezGeneId;

    private String ncbiChromosome;

    private Long ncbiStart;

    private Long ncbiStop;

    @Pattern(regexp = "^([\\+-\\?]{1}|)$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ncbiStrand;

    private String ensemblGeneId;

    private String ensemblChromosome;

    private Long ensemblStart;

    private Long ensemblStop;

    @Pattern(regexp = "^([\\+-\\?]{1}|)$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ensemblStrand;

    @ManyToMany(mappedBy = "intendedMouseGenes")
    @JsonBackReference
    private Set<Project> projects;

    @ManyToMany
    @JoinTable(
            name = "intended_mouse_gene_flag",
            joinColumns = @JoinColumn(name = "mouse_gene_id"),
            inverseJoinColumns = @JoinColumn(name = "gene_flag_id"))
    private Set<GeneFlag> geneFlags;
}
