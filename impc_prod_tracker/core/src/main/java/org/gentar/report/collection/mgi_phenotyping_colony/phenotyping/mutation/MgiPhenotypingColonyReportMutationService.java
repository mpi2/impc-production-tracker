package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.mutation;

import java.util.List;

public interface MgiPhenotypingColonyReportMutationService {

    /**
     *
     * @return a list of MgiPhenotypingColonyReportMutationGeneProjection Spring database projections containing
     *         an Mutation Id and the associated Gene Id and Gene Symbol
     */
    List<MgiPhenotypingColonyReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds);
}
