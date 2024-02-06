package org.gentar.report.collection.gene_interest;

import org.gentar.biology.project.assignment.AssignmentStatusServiceImpl;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GeneInterestReportMergeHelperImpl implements GeneInterestReportMergeHelper {

    private final AssignmentStatusServiceImpl assignmentStatusService;

    public GeneInterestReportMergeHelperImpl(
            AssignmentStatusServiceImpl assignmentStatusService)
    {
        this.assignmentStatusService = assignmentStatusService;
    }

    @Override
    public Map<String, String> mergeSummaryAssignmentForGenes (Map<String, String> summaryA,
                                                               Map<String, String> summaryB)
    {
        Comparator<String> byRanking =
                Comparator.comparing((String summary) -> assignmentStatusService
                        .getAssignmentStatusOrderingMap()
                        .get(summary));

        return Stream.concat(
                summaryA.entrySet().stream(),summaryB.entrySet().stream() )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> Stream.of(v1,v2).min(byRanking).get()
                ));

    }

    @Override
    public Map<String, String> mergeGenes(Map<String, String> geneMapA,
                                          Map<String, String> geneMapB)
    {
        return Stream.concat(
                geneMapA.entrySet().stream(),geneMapB.entrySet().stream() )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1
                ));
    }
}
