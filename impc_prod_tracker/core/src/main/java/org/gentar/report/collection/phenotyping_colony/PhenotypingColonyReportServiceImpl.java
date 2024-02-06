package org.gentar.report.collection.phenotyping_colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.phenotyping_colony.phenotyping.PhenotypingColonyReportPhenotypingServiceImpl;
import org.gentar.report.collection.phenotyping_colony.phenotyping.outcome.PhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.phenotyping_colony.phenotyping.phenotyping_attempt.PhenotypingColonyReportPhenotypingAttemptProjection;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PhenotypingColonyReportServiceImpl implements PhenotypingColonyReportService {

    private final PhenotypingColonyReportPhenotypingServiceImpl phenotypingColonyReportService;
    private final ReportServiceImpl reportService;

    private List<String> reportRows;
    private List<PhenotypingColonyReportPhenotypingAttemptProjection> pap;
    private Map<Long, PhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;

    public PhenotypingColonyReportServiceImpl(PhenotypingColonyReportPhenotypingServiceImpl phenotypingColonyReportService,
                                              ReportServiceImpl reportService) {
        this.phenotypingColonyReportService = phenotypingColonyReportService;
        this.reportService = reportService;
    }


    @Override
    public void generatePhenotypingColonyReport() {

        pap = phenotypingColonyReportService.getPhenotypingColonyReportPhenotypingAttemptProjections();
        filteredOutcomeMutationMap = phenotypingColonyReportService.getMutationMap();
        filteredMutationGeneMap = phenotypingColonyReportService.getGeneMap();

        reportRows = prepareReport();
        saveReport();
    }

    private List<String> prepareReport( ) {
        List<PhenotypingColonyReportPhenotypingAttemptProjection> sortedEntries =
                pap.stream()
                .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
                .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()))
                .sorted(Comparator.comparing(x -> filteredMutationGeneMap.get(
                        filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()).getSymbol()
                        )
                ).toList();

        return sortedEntries
                .stream()
                .map(this::constructRow)
                .collect(Collectors.toList());
    }

    private String constructRow(PhenotypingColonyReportPhenotypingAttemptProjection x) {
        String mutationSymbol = filteredOutcomeMutationMap.get(x.getOutcomeId()).getSymbol();
        Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId());
        String cohortProductionWorkUnit = x.getCohortProductionWorkUnit() == null ? x.getPhenotypingWorkUnit() : x.getCohortProductionWorkUnit();
        return g.getSymbol() + "\t" +
                        g.getAccId() + "\t" +
                        x.getColonyName() + "\t" +
                        x.getEsCellName() + "\t" +
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
        reportService.saveReport(ReportTypeName.PHENOTYPING_COLONIES, report);
    }

    private String assembleReport() {

        String header = generateReportHeaders();
        String report = String.join("\n", reportRows);

        return String.join("\n", header, report);

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

        return String.join("\t", headers);

    }

}
