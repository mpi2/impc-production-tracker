package org.gentar.report.collection.gene_interest.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneInterestReportOutcomeServiceImpl implements GeneInterestReportOutcomeService {

    private final GeneInterestReportOutcomeRepository geneInterestReportOutcomeRepository;

    public GeneInterestReportOutcomeServiceImpl(GeneInterestReportOutcomeRepository geneInterestReportOutcomeRepository) {
        this.geneInterestReportOutcomeRepository = geneInterestReportOutcomeRepository;
    }

    @Override
    public List<GeneInterestReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds ) {
        return geneInterestReportOutcomeRepository.findSelectedOutcomeMutationProjectionsForGeneInterestReport(outcomeIds);
    }
}
