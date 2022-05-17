package org.gentar.biology.targ_rep.production_qc;

import org.gentar.biology.targ_rep.TargRepEsCellResponseDTO;
import org.springframework.stereotype.Component;

/**
 * DistributionQcMapper.
 */
@Component
public class ProductionQcMapper {

    private final TargRepProductionQcService targRepProductionQcService;

    public ProductionQcMapper(TargRepProductionQcService targRepProductionQcService) {
        this.targRepProductionQcService = targRepProductionQcService;
    }

    public TargRepEsCellResponseDTO toProductionQcServiceDto(TargRepEsCellResponseDTO esCellDto) {
        TargRepProductionQc targRepProductionQc =
            getProductionQc(esCellDto.getId());
        if (targRepProductionQc != null) {
            esCellDto.setProductionQcFivePrimeScreen(targRepProductionQc.getFivePrimeScreen());
            esCellDto.setProductionQcLossOfAllele(targRepProductionQc.getLossOfAllele());
            esCellDto.setProductionQcLoxpScreen(targRepProductionQc.getLoxpScreen());
            esCellDto.setProductionQcThreePrimeScreen(targRepProductionQc.getThreePrimeScreen());
            esCellDto.setProductionQcVectorIntegrity(targRepProductionQc.getVectorIntegrity());
        }
        return esCellDto;
    }

    private TargRepProductionQc getProductionQc(Long esCEllId) {
        return targRepProductionQcService.getTargRepProductionQcByEsCellId(esCEllId);
    }

}
