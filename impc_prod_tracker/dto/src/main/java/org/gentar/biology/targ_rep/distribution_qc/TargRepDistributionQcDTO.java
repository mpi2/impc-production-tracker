package org.gentar.biology.targ_rep.distribution_qc;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellDistributionCentreDTO;
import org.springframework.hateoas.server.core.Relation;


/**
 * TargRepDistributionQcDTO.
 */
@Relation(collectionRelation = "targrep_distribution_qc")
@Data
@RequiredArgsConstructor
public class TargRepDistributionQcDTO {

    private Long id;
    private Double karyotypeHigh;
    private Double karyotypeLow;
    private String copyNumber;
    private String fivePrimeLrPcr;
    private String fivePrimeSrPcr;
    private String thawing;
    private String threePrimeLrPcr;
    private String threePrimeSrPcr;
    private String loa;
    private String loxp;
    private String lacz;
    private String chr1;
    private String chr8a;
    private String chr8b;
    private String chr11a;
    private String chr11b;
    private String chry;
    private TargRepEsCellDistributionCentreDTO esCellDistributionCentre;
}
