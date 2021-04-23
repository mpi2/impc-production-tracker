package org.gentar.biology.targ_rep.allele;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.allele.genebank_file.TargRepGenebankFile;
import org.gentar.biology.targ_rep.allele.mutation_method.TargRepMutationMethod;
import org.gentar.biology.targ_rep.allele.mutation_subtype.TargRepMutationSubtype;
import org.gentar.biology.targ_rep.allele.mutation_type.TargRepMutationType;
import org.gentar.biology.targ_rep.gene.TargRepGene;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepAllele extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepAlleleSeq", sequenceName = "TARG_REP_ALLELE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepAlleleSeq")
    private Long id;

    @ToString.Exclude
    @NotNull
    @ManyToOne(targetEntity= TargRepGene.class)
    private TargRepGene gene;

    @NotNull
//    @Value("Default GRCm38")
    private String assembly;

    @NotNull
    private String chromosome;

    @NotNull
    private String strand;

    private Integer homologyArmStart;

    private Integer homologyArmEnd;

    private Integer loxpStart;

    private Integer loxpEnd;

    private Integer cassetteStart;

    private Integer cassetteEnd;

    private String cassette;

    private String backbone;

    private String subtypeDescription;

    private String floxedStartExon;

    private String floxedEndExon;

    private Integer projectDesignId;

    private String reporter;

    @NotNull
    @ManyToOne
    private TargRepMutationMethod mutationMethod;

    @NotNull
    @ManyToOne
    private TargRepMutationType mutationType;

    @ManyToOne
    private TargRepMutationSubtype mutationSubtype;

    private String cassetteType;

    private Integer intron;

    private String type;

    @NotNull
    @Column(columnDefinition = "boolean default false")
    private Boolean hasIssue;

    @Column(columnDefinition = "TEXT")
    private String issueDescription;

    @Column(columnDefinition = "TEXT")
    private String sequence;

    private String taqmanCriticalDelAssayId;

    private String taqmanUpstreamDelAssayId;

    private String taqmanDownstreamDelAssayId;

    private String wildtypeOligosSequence;

    @OneToOne
    private TargRepGenebankFile alleleGenbankFile;

    @OneToOne
    private TargRepGenebankFile vectorGenbankFile;
}
