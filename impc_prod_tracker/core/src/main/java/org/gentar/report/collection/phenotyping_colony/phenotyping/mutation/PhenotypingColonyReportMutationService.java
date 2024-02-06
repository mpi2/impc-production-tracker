package org.gentar.report.collection.phenotyping_colony.phenotyping.mutation;

import java.util.List;

public interface PhenotypingColonyReportMutationService {

    /**
     *
     * @return a list of PhenotypingColonyReportMutationGeneProjection Spring database projections containing
     *         an Mutation Id and the associated Gene Id and Gene Symbol
     */
    List<PhenotypingColonyReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds);
}
