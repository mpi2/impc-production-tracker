package org.gentar.biology.gene;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.gene.flag.GeneFlag;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.species.Species;
import jakarta.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Gene extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "geneSeq", sequenceName = "GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geneSeq")
    private Long id;

    private String symbol;

    private String name;

    private String type;

    private String genomeBuild;

    private Long entrezGeneId;

    private String ncbiChromosome;

    private Long ncbiStart;

    private Long ncbiStop;

    @Pattern(regexp = "^([+-?]{1}|)$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ncbiStrand;

    private String ensemblGeneId;

    private String ensemblChromosome;

    private Long ensemblStart;

    private Long ensemblStop;

    @Pattern(regexp = "^([+-?]{1}|)$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ensemblStrand;

    private String mgiCm;

    private String mgiChromosome;

    private Long mgiStart;

    private Long mgiStop;

    @Pattern(regexp = "^([+-?]{1}|)$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String mgiStrand;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "genes")
    private Set<Mutation> mutations;

    @ManyToOne(targetEntity= Species.class)
    private Species species;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "gene_flag_relation",
            joinColumns = @JoinColumn(name = "gene_id"),
            inverseJoinColumns = @JoinColumn(name = "gene_flag_id"))
    private Set<GeneFlag> geneFlags;

    private String accId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @IgnoreForAuditingChanges
    @OneToMany
    @JoinColumn(name = "gene_id")
    private Set<ProjectIntentionGene> projectIntentionGenes;
}
