package org.gentar.biology.targ_rep.allele;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
public class TargRepAllele extends Serializers.Base
{
    @Id
    @SequenceGenerator(name = "projectTypeSeq", sequenceName = "PROJECT_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectTypeSeq")
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

    @NotNull
    private Integer homologyArmStart;

    @NotNull
    private Integer homologyArmEnd;

    private Integer loxpStart;

    private Integer loxpEnd;

    @NotNull
    private Integer cassetteStart;

    @NotNull
    private Integer cassetteEnd;

    @NotNull
    private String cassette;

    private String backbone;

    private String subtypeDescription;

    private String floxedStartExon;

    private String floxedEndExon;

    private String projectDesignId;

    private String reporter;

    @NotNull
    @ManyToOne
    private TargRepMutationMethod mutationMethod;

    @NotNull
    @ManyToOne
    private TargRepMutationType mutationType;

    @NotNull
    @ManyToOne
    private TargRepMutationSubtype mutationSubtype;

    @NotNull
    private String cassetteType;

    private Integer intron;

    private String type;

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
