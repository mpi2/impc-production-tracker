package org.gentar.report.collection.mgi_phenotyping_colony;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.MgiPhenotypingColonyReportPhenotypingServiceImpl;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.outcome.MgiPhenotypingColonyReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.phenotyping_attempt.MgiPhenotypingColonyReportPhenotypingAttemptProjection;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MgiPhenotypingColonyReportServiceImpl implements MgiPhenotypingColonyReportService {

    private final MgiPhenotypingColonyReportPhenotypingServiceImpl phenotypingColonyReportService;
    private final ReportServiceImpl reportService;

    private List<String> reportRows;
    private List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> pap;
    private Map<Long, MgiPhenotypingColonyReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;

    public MgiPhenotypingColonyReportServiceImpl(MgiPhenotypingColonyReportPhenotypingServiceImpl phenotypingColonyReportService,
                                                 ReportServiceImpl reportService)
    {
        this.phenotypingColonyReportService = phenotypingColonyReportService;
        this.reportService = reportService;
    }

    @Override
    public void generateMgiPhenotypingColonyReport() {

        pap = phenotypingColonyReportService.getPhenotypingColonyReportPhenotypingAttemptProjections();
        filteredOutcomeMutationMap = phenotypingColonyReportService.getMutationMap();
        filteredMutationGeneMap = phenotypingColonyReportService.getGeneMap();

        reportRows = prepareReport();
        saveReport();
    }

    private List<String> prepareReport( ) {
        List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> sortedEntries =
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

    private String constructRow(MgiPhenotypingColonyReportPhenotypingAttemptProjection x) {
        String mutationSymbol = filteredOutcomeMutationMap.get(x.getOutcomeId()).getSymbol();
        String mutationMin = filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationIdentificationNumber();
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
                mutationSymbol + "\t" +
                mutationMin;
    }

    private void saveReport() {

        String report = assembleReport();
        reportService.saveReport(ReportTypeName.MGI_PHENOTYPING, report);
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
                "Mutation Symbol",
                "GenTaR Mutation Identifier"
        );

        return String.join("\t", headers);

    }
}
