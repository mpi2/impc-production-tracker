package org.gentar.report.collection.mgi_crispr_allele;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.common.phenotyping.phenotyping_attempt.CommonPhenotypingColonyReportPhenotypingAttemptProjection;
import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyProjection;
import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MgiCrisprAlleleReportServiceImpl implements MgiCrisprAlleleReportService {

    private final MgiCrisprAlleleReportColonyServiceImpl colonyReportService;
    private final ReportServiceImpl reportService;

    private List<String> reportRows;
    private List<MgiCrisprAlleleReportColonyProjection> cp;
    private Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;

    public MgiCrisprAlleleReportServiceImpl(MgiCrisprAlleleReportColonyServiceImpl colonyReportService, ReportServiceImpl reportService) {
        this.colonyReportService = colonyReportService;
        this.reportService = reportService;
    }

    @Override
    public void generateMgiCrisprAlleleReport() {

        cp = colonyReportService.getAllColonyReportProjections();
        filteredOutcomeMutationMap = colonyReportService.getMutationMap();
        filteredMutationGeneMap = colonyReportService.getGeneMap();

        reportRows = prepareReport();
        saveReport();
    }


    private List<String> prepareReport() {
        List<MgiCrisprAlleleReportColonyProjection> sortedEntries =
                cp.stream()
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

    private String constructRow(MgiCrisprAlleleReportColonyProjection x) {
        String mutationSymbol = filteredOutcomeMutationMap.get(x.getOutcomeId()).getSymbol();
        String mutationMgiAlleleAccId = filteredOutcomeMutationMap.get(x.getOutcomeId()).getMgiAlleleAccId();
        String mgiAlleleAccId = mutationMgiAlleleAccId == null ? "" : mutationMgiAlleleAccId;
        String mutationCategory = filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationCategory();
        String mutationType = filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationType();
        Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId());
        return g.getSymbol() + "\t" +
                g.getAccId() + "\t" +
                x.getStrainName() + "\t" +
                x.getColonyName() + "\t" +
                x.getStrainName() + "\t" +
                "IMPC" + "\t" +
                x.getProductionWorkUnit() + "\t" +
                "endonuclease-mediated" + "\t" +
                mutationCategory + "\t" +
                mutationType + "\t" +
                "description" + "\t" +
                mutationSymbol + "\t" +
                mgiAlleleAccId;
    }

    private void saveReport() {

        String report = assembleReport();
        reportService.saveReport(ReportTypeName.MGI_CRISPR, report);
    }

    private String assembleReport() {

        String header = generateReportHeaders();
        String report = reportRows
                .stream()
                .collect(Collectors.joining("\n"));

        return Arrays.asList(header, report).stream().collect(Collectors.joining("\n"));

    }

    private String generateReportHeaders() {
        List<String> headers = Arrays.asList(
                "Gene Symbol",
                "Gene MGI Accession ID",
                "ES Cell Line",
                "Colony Name",
                "Colony Background Strain",
                "Project Name",
                "Production Work Unit",
                "Mutation Class",
                "Mutation Type",
                "Mutation Subtype",
                "Mutation Description",
                "Mutation Symbol",
                "Mutation MGI Accession ID"
        );

        String headerString = headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;

    }
}