package org.gentar.biology.targ_rep.production_qc;

import org.springframework.stereotype.Component;

@Component
public class TargRepTargRepProductionQcServiceImpl implements TargRepProductionQcService {

    private final TargRepProductionQcRepository targRepProductionQcRepository;

    public TargRepTargRepProductionQcServiceImpl(
        TargRepProductionQcRepository targRepProductionQcRepository) {
        this.targRepProductionQcRepository = targRepProductionQcRepository;
    }


    /**
     * @param esCellId
     * @return a list of GeneInterestReportMutationGeneProjection Spring database projections containing
     * an Mutation Id and the associated Gene Id and Gene Symbol
     */
    @Override
    public TargRepProductionQc getTargRepProductionQcByEsCellId(Long esCellId) {
        return targRepProductionQcRepository.findTargRepProductionQcByEsCellId(esCellId);
    }


}
