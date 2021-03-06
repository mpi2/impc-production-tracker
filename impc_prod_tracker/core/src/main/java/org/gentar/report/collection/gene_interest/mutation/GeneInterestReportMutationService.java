package org.gentar.report.collection.gene_interest.mutation;

import java.util.List;

public interface GeneInterestReportMutationService {

    /**
     *
     * @param mutationIds
     * @return a list of GeneInterestReportMutationGeneProjection Spring database projections containing
     *         an Mutation Id and the associated Gene Id and Gene Symbol
     */
    List<GeneInterestReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds);
}
