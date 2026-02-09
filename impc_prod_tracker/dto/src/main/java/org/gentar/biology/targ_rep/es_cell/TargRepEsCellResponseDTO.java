package org.gentar.biology.targ_rep.es_cell;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.targ_rep.distribution_qc.TargRepDistributionQcDTO;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targrep_es_cells")
@Data
@RequiredArgsConstructor
public class TargRepEsCellResponseDTO extends RepresentationModel<TargRepEsCellResponseDTO>
{
    private Long id;
    private Long pipelineId;
    private Long alleleId;
    private String name;
    private Long targetingVectorId;
    private String parentalCellLine;
    private String ikmcProjectId;
    private String mgiAlleleId;
    private String alleleSymbolSuperscript;
    private String comment;
    private String contact;
    private Boolean reportToPublic;
    private String strainName;
    private String productionQcFivePrimeScreen;
    private String productionQcLossOfAllele;
    private String productionQcLoxpScreen;
    private String productionQcThreePrimeScreen;
    private String productionQcVectorIntegrity;
    private String userQcComment;
    private String userQcFivePrimeCassetteIntegrity;
    private String userQcFivePrimeLrPcr;
    private String userQcKaryotype;
    private String userQcLaczSrPcr;
    private String userQcLossOfWtAllele;
    private String userQcLoxpConfirmation;
    private String userQcMapTest;
    private String userQcMutantSpecificSrPcr;
    private String userQcNeoCountQpcr;
    private String userQcNeoSrPcr;
    private String userQcSouthernBlot;
    private String userQcThreePrimeLrPcr;
    private String userQcTvBackboneAssay;
    List<TargRepDistributionQcDTO> targRepDistributionQcList;
    List<TargRepEsCellDistributionProductDTO> targRepEsCellDistributionProductList;
}
