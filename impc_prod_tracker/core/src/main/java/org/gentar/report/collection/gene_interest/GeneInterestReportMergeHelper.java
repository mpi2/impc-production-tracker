package org.gentar.report.collection.gene_interest;

import java.util.Map;

public interface GeneInterestReportMergeHelper {

    Map<String, String> mergeSummaryAssignmentForGenes(Map<String, String> summaryA,
                                                       Map<String, String> summaryB);

    Map<String, String> mergeGenes(Map<String, String> geneMapA,
                                   Map<String, String> geneMapB);

}
