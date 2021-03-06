package org.gentar.report.collection.gene_interest.phenotyping_attempt;

import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationServiceImpl;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneInterestReportPhenotypingAttemptServiceImpl implements GeneInterestReportPhenotypingAttemptService {

    private final GeneInterestReportPhenotypingAttemptRepository geneInterestReportPhenotypingAttemptRepository;

    public GeneInterestReportPhenotypingAttemptServiceImpl(GeneInterestReportPhenotypingAttemptRepository geneInterestReportPhenotypingAttemptRepository) {
        this.geneInterestReportPhenotypingAttemptRepository = geneInterestReportPhenotypingAttemptRepository;
    }


    @Override
    public List<GeneInterestReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections() {
        return geneInterestReportPhenotypingAttemptRepository.findAllGeneInterestReportPhenotypingAttemptProjections();
    }
}
