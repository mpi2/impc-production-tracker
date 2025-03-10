package org.gentar.biology.mutation.aligned_fasta;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class AlignedFasta extends BaseEntity {
    @Id
    @SequenceGenerator(name = "alignedFastaSeq", sequenceName = "ALIGNED_FASTA_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alignedFastaSeq")
    private Long id;
    private String chrom;
    private Integer chromStart;
    private Integer chromEnd;
    private String name;
    private Integer score;
    private String strand;
    private Integer thickStart;
    private Integer thickEnd;
    private Integer itemRgb;
    private Integer blockCount;
    private String blockSizes;
    private String blockStarts;

    @NotNull
    @ToString.Exclude
    @ManyToOne(targetEntity= Mutation.class)
    private Mutation mutation;
}

