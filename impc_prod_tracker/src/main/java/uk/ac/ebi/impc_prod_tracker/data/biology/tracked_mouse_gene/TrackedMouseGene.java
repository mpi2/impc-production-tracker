package uk.ac.ebi.impc_prod_tracker.data.biology.tracked_mouse_gene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_mouse_allele.TrackedMouseAllele;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class TrackedMouseGene extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "trackedMouseGeneSeq", sequenceName = "TRACKED_MOUSE_GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackedMouseGeneSeq")
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

    @ManyToMany
    @JoinTable(
        name = "tracked_mouse_gene_allele",
        joinColumns = @JoinColumn(name = "tracked_mouse_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "tracked_mouse_allele_id"))
    private Set<TrackedMouseAllele> trackedMouseAlleles;
}
