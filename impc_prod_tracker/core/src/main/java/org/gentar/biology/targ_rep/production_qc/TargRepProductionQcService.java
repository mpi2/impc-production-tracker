package org.gentar.biology.targ_rep.production_qc;

public interface TargRepProductionQcService {

    /**
     * @param esCellId
     * @return a list of GeneInterestReportMutationGeneProjection Spring database projections containing
     * an Mutation Id and the associated Gene Id and Gene Symbol
     */
    TargRepProductionQc getTargRepProductionQcByEsCellId(Long esCellId);

}