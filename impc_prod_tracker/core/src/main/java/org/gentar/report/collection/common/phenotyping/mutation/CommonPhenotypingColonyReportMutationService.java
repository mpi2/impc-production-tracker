package org.gentar.report.collection.common.phenotyping.mutation;

import java.util.List;

public interface CommonPhenotypingColonyReportMutationService {

    /**
     *
     * @param mutationIds
     * @return a list of CommonPhenotypingColonyReportMutationGeneProjection Spring database projections containing
     *         an Mutation Id and the associated Gene Id and Gene Symbol
     */
    List<CommonPhenotypingColonyReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds);
}
