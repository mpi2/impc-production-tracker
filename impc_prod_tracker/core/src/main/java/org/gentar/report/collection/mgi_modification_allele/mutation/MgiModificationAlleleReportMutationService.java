package org.gentar.report.collection.mgi_modification_allele.mutation;

import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportEsCellMutationTypeProjection;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationGeneProjection;

import java.util.List;

public interface MgiModificationAlleleReportMutationService {

    /**
     *
     * @param mutationIds
     * @return a list of MgiModificationAlleleReportMutationGeneProjection Spring database projections containing
     *         an Mutation Id and the associated Gene Id and Gene Symbol
     */
    List<MgiModificationAlleleReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds);

    /**
     *
     * @param mutationGeneProjections
     * @return a list of MgiModificationAlleleReportEsCellMutationTypeProjection Spring database projections containing
     *         an Mutation Id, Mutation Identification Number and the Mutation Categorization Name for the ES Cell allele type
     */
    List<MgiModificationAlleleReportEsCellMutationTypeProjection> getSelectedEsCellMutationTypeProjections(
            List<MgiModificationAlleleReportMutationGeneProjection> mutationGeneProjections);
}
