package uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene_flag.GeneFlag;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_allele.MouseAllele;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene_synomym.MouseGeneSynonym;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.flag.PlanFlag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MouseGene extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mouseGeneSeq", sequenceName = "MOUSE_GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mouseGeneSeq")
    private Long id;

    private String name;

    private String symbol;

    private String mgiId;

    @NotNull
    private String chromosome;

    @NotNull
    private Long start;

    @NotNull
    private Long stop;

    @Pattern(regexp = "^[\\+-\\?]{1}$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String strand;

    private String genomeBuild;

    @ManyToMany
    @JoinTable(
        name = "mouse_gene_allele",
        joinColumns = @JoinColumn(name = "mouse_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "mouse_allele_id"))
    private Set<MouseAllele> mouseAlleles;

    @ManyToMany
    @JoinTable(
        name = "mouse_gene_synonym_relation",
        joinColumns = @JoinColumn(name = "mouse_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "mouse_gene_synonym_id"))
    private Set<MouseGeneSynonym> mouseGeneSynonyms;

    @ManyToMany
    @JoinTable(
        name = "mouse_gene_flag",
        joinColumns = @JoinColumn(name = "mouse_gene_id"),
        inverseJoinColumns = @JoinColumn(name = "gene_flag_id"))
    private Set<GeneFlag> mouseGeneFlags;

    @ManyToMany(mappedBy = "mouseOrthologs")
    private Set<HumanGene> humanOrthologs;
}
