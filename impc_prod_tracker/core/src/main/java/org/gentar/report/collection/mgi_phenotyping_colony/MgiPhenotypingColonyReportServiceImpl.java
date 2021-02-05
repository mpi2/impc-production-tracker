package org.gentar.report.collection.mgi_phenotyping_colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.common.phenotyping.CommonPhenotypingColonyReportServiceImpl;
import org.gentar.report.collection.common.phenotyping.outcome.CommonPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.common.phenotyping.phenotyping_attempt.CommonPhenotypingColonyReportPhenotypingAttemptProjection;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MgiPhenotypingColonyReportServiceImpl implements MgiPhenotypingColonyReportService {

    private final CommonPhenotypingColonyReportServiceImpl phenotypingColonyReportService;
    private final ReportServiceImpl reportService;

    private List<String> reportRows;
    private List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> pap;
    private Map<Long, CommonPhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;

    public MgiPhenotypingColonyReportServiceImpl(CommonPhenotypingColonyReportServiceImpl phenotypingColonyReportService,
                                                 ReportServiceImpl reportService)
    {
        this.phenotypingColonyReportService = phenotypingColonyReportService;
        this.reportService = reportService;
    }


    public void generateMgiPhenotypingColonyReport() {

        pap = phenotypingColonyReportService.getPhenotypingColonyReportPhenotypingAttemptProjections();
        filteredOutcomeMutationMap = phenotypingColonyReportService.getMutationMap();
        filteredMutationGeneMap = phenotypingColonyReportService.getGeneMap();

        reportRows = prepareReport();
        saveReport();
    }

    private List<String> prepareReport( ) {
        List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> sortedEntries =
                pap.stream()
                        .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
                        .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()))
                        .sorted(Comparator.comparing(x -> filteredMutationGeneMap.get(
                                filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()).getSymbol()
                                )
                        ).collect(Collectors.toList());

        return sortedEntries
                .stream()
                .map(x -> constructRow(x))
                .collect(Collectors.toList());
    }

    private String constructRow(CommonPhenotypingColonyReportPhenotypingAttemptProjection x) {
        String mutationSymbol = filteredOutcomeMutationMap.get(x.getOutcomeId()).getSymbol();
        Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId());
        String cohortProductionWorkUnit = x.getCohortProductionWorkUnit() == null ? x.getPhenotypingWorkUnit() : x.getCohortProductionWorkUnit();
        return g.getSymbol() + "\t" +
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
                mutationSymbol;
    }

    private void saveReport() {

        String report = assembleReport();
        reportService.saveReport(ReportTypeName.MGI_PHENOTYPING, report);
    }

    private String assembleReport() {

        String header = generateReportHeaders();
        String report = reportRows
                .stream()
                .collect(Collectors.joining("\n"));

        return Arrays.asList(header,report).stream().collect(Collectors.joining("\n"));

    }

    private String generateReportHeaders() {
        List<String> headers = Arrays.asList(
                "Gene Symbol",
                "MGI Gene Accession ID",
                "Phenotyping External Reference",
                "ES Cell Name",
                "Background Strain",
                "MGI Strain Accession ID",
                "Production Work Unit",
                "Production Work Group",
                "Phenotyping Work Unit",
                "Phenotyping Work Group",
                "Cohort Work Unit",
                "Mutation Symbol"
        );

        String headerString =   headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;

    }
}
