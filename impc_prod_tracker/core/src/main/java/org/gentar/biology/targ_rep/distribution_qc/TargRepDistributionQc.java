package org.gentar.biology.targ_rep.distribution_qc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.distribution_qc.distribution_centre.TargRepEsCellDistributionCentre;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;


/**
 * TargRepDistributionQc.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepDistributionQc extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepDistributionQcSeq",
        sequenceName = "TARG_REP_DISTRIBUTION_QC_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepDistributionQcSeq")
    private Long id;

    private String fivePrimeSrPcr;

    private String threePrimeSrPcr;

    private Double karyotypeLow;

    private Double karyotypeHigh;

    private String copyNumber;

    private String fivePrimeLrPcr;

    private String threePrimeLrPcr;

    private String thawing;

    private String loa;

    private String loxp;

    private String lacz;

    private String chr1;

    private String chr8a;

    private String chr8b;

    private String chr11a;

    private String chr11b;

    private String chry;

    @ToString.Exclude
    @NotNull
    @ManyToOne(targetEntity = TargRepEsCell.class)
    private TargRepEsCell esCell;

    @ToString.Exclude
    @NotNull
    @ManyToOne(targetEntity = TargRepEsCellDistributionCentre.class)
    private TargRepEsCellDistributionCentre esCellDistributionCentre;

    private String loxpSrpcr;

    private String neoQpcr;
}
