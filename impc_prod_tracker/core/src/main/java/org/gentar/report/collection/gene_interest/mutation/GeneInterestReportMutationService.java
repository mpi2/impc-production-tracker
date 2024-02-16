package org.gentar.report.collection.gene_interest.mutation;

import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;

import java.util.List;

public interface GeneInterestReportMutationService {

    /**
     *
     * @return a list of GeneInterestReportMutationGeneProjection Spring database projections containing
     *         an Mutation Id and the associated Gene Id and Gene Symbol
     */
    List<GeneInterestReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds);

    /**
     *
     * @return a list of GeneInterestReportEsCellMutationTypeProjection Spring database projections containing
     *         an Mutation Id, Mutation Identification Number and the Mutation Categorization Name for the ES Cell allele type
     */
    List<GeneInterestReportEsCellMutationTypeProjection> getSelectedEsCellMutationTypeProjections(List<GeneInterestReportGeneProjection> geneProjections);
}
