package org.gentar.biology.plan.attempt.crispr.guide;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ImputedGuideData extends BaseEntity {

    @Id
    @SequenceGenerator(name = "imputedGuideDataSeq", sequenceName = "IMPUTED_GUIDE_DATA_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imputedGuideDataSeq")
    private Long id;

    private String centre;
    private String geneSymbol;
    private String tpn;
    private String pin;
    private String status;
    private String gid;
    private String genomeBuild;
    private String chrom;
    private Integer chromStart;
    private Integer chromEnd;
    private String strand;
    private String sequence;
    private String pam;
    private String guideSequence;
    private String colonyName;
    private String alleleSymbol;
    private String fasta;
    private String conclusion;

}
