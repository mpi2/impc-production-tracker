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

        Map<String, String> summaryAssignmentMap = Stream.concat(
                summaryA.entrySet().stream(),summaryB.entrySet().stream() )
                .collect(Collectors.toMap(
                        x -> x.getKey(),
                        x -> x.getValue(),
                        (v1, v2) -> Arrays.asList(v1,v2).stream().min(byRanking).get()
                ));

        return summaryAssignmentMap;

    }

    @Override
    public Map<String, String> mergeGenes(Map<String, String> geneMapA,
                                          Map<String, String> geneMapB)
    {
        return Stream.concat(
                geneMapA.entrySet().stream(),geneMapB.entrySet().stream() )
                .collect(Collectors.toMap(
                        x -> x.getKey(),
                        x -> x.getValue(),
                        (v1, v2) -> v1
                ));
    }
}
