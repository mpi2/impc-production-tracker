package org.gentar.report.collection.gene_interest.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptProjection;

import java.util.List;
import java.util.Map;

public interface GeneInterestReportPhenotypingService {

    Map<Long, GeneInterestReportOutcomeMutationProjection> getMutationMap();

    Map<Long, Gene> getGeneMap();

    List<GeneInterestReportPhenotypingAttemptProjection> getGeneInterestReportPhenotypingAttemptProjections();
}
