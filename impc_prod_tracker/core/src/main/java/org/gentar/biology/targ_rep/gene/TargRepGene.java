package org.gentar.biology.targ_rep.gene;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;



/**
 * TargRepGene.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepGene extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepGeneSeq", sequenceName = "TARG_REP_GENE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepGeneSeq")
    private Long id;

    private String symbol;

    private String name;

    private String type;

    private String genomeBuild;

    private Long entrezGeneId;

    private String ncbiChromosome;

    private Long ncbiStart;

    private Long ncbiStop;

    @Pattern(regexp = "^([\\+-\\?]{1}|)$",
        message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ncbiStrand;

    private String ensemblGeneId;

    private String ensemblChromosome;

    private Long ensemblStart;

    private Long ensemblStop;

    @Pattern(regexp = "^([\\+-\\?]{1}|)$",
        message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String ensemblStrand;

    private String mgiCm;

    private String mgiChromosome;

    private Long mgiStart;

    private Long mgiStop;

    @Pattern(regexp = "^([\\+-\\?]{1}|)$",
        message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String mgiStrand;

    private String accId;
}
