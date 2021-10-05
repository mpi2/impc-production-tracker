package org.gentar.report.collection.gene_interest.phenotyping;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptProjection;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GeneInterestReportPhenotypingService {

    Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> getMutationMap();

    Map<Long, GeneInterestReportOutcomeMutationProjection> getFilteredMutationMap(
            Map<Long, Set<GeneInterestReportOutcomeMutationProjection>> completeMutationMap);

    Map<Long, Set<Gene>> getGeneMap();

    Map<Long, Gene> getFilteredGeneMap(Map<Long, Set<Gene>> completeMutationGeneMap);

    List<GeneInterestReportPhenotypingAttemptProjection> getGeneInterestReportPhenotypingAttemptProjections();
}
