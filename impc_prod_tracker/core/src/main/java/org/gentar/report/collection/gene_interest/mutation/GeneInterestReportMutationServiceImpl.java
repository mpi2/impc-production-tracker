package org.gentar.report.collection.gene_interest.mutation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneInterestReportMutationServiceImpl implements GeneInterestReportMutationService
{

    private final GeneInterestReportMutationRepository geneInterestReportMutationRepository;

    public GeneInterestReportMutationServiceImpl(
            GeneInterestReportMutationRepository geneInterestReportMutationRepository)
    {
        this.geneInterestReportMutationRepository = geneInterestReportMutationRepository;
    }


    public List<GeneInterestReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds){
        return geneInterestReportMutationRepository.findSelectedMutationGeneProjectionsForGeneInterestReport(mutationIds);
    }
}
