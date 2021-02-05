package org.gentar.report.collection.mgi_phenotyping_colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.collection.common.phenotyping.CommonPhenotypingColonyReportServiceImpl;
import org.gentar.report.collection.common.phenotyping.mutation.CommonPhenotypingColonyReportMutationServiceImpl;
import org.gentar.report.collection.common.phenotyping.outcome.CommonPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.common.phenotyping.outcome.CommonPhenotypingColonyReportOutcomeServiceImpl;
import org.gentar.report.collection.common.phenotyping.phenotyping_attempt.CommonPhenotypingColonyReportPhenotypingAttemptProjection;
import org.gentar.report.collection.common.phenotyping.phenotyping_attempt.CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MgiPhenotypingColonyReportServiceImpl implements MgiPhenotypingColonyReportService {

    private final CommonPhenotypingColonyReportServiceImpl phenotypingColonyReportService;

    public MgiPhenotypingColonyReportServiceImpl(CommonPhenotypingColonyReportServiceImpl phenotypingColonyReportService)
    {
        this.phenotypingColonyReportService = phenotypingColonyReportService;
    }


    public void generateMgiPhenotypingColonyReport() {

        List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> pap = phenotypingColonyReportService.getPhenotypingColonyReportPhenotypingAttemptProjections();

        Map<Long, CommonPhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap = phenotypingColonyReportService.getMutationMap();

        Map<Long, Gene> filteredMutationGeneMap = phenotypingColonyReportService.getGeneMap();
        writeReport(pap, filteredOutcomeMutationMap, filteredMutationGeneMap);

    }


    private void writeReport( List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> pap,
                              Map<Long, CommonPhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap,
                              Map<Long, Gene> filteredMutationGeneMap ) {
        pap.stream()
                .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
                .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()))
                .forEach(x -> {
                    String mutationSymbol = filteredOutcomeMutationMap.get(x.getOutcomeId()).getSymbol();
                    Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId());
                    String cohortProductionWorkUnit = x.getCohortProductionWorkUnit() == null ? x.getPhenotypingWorkUnit() : x.getCohortProductionWorkUnit();
                    System.out.println(
                            g.getSymbol() + "\t" +
                                    g.getAccId() + "\t" +
                                    x.getColonyName() + "\t" +
                                    "\t" +
                                    x.getStrainName() + "\t" +
                                    x.getStrainAccId() + "\t" +
                                    x.getProductionWorkUnit() + "\t" +
                                    x.getProductionWorkGroup() + "\t" +
                                    x.getPhenotypingWorkUnit() + "\t" +
                                    x.getPhenotypingWorkGroup() + "\t" +
                                    cohortProductionWorkUnit + "\t" +
                                    mutationSymbol

                    );
                });
    }
}
