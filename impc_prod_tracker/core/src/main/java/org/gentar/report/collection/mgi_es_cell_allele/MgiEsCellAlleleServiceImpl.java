package org.gentar.report.collection.mgi_es_cell_allele;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MgiEsCellAlleleServiceImpl implements MgiEsCellAlleleService{

    private final ReportServiceImpl reportService;

    private List<String> reportRows;

    public MgiEsCellAlleleServiceImpl(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @Override
    public void generateMgiEsCellAlleleReport() {
        reportRows = prepareReport();
        saveReport();
    }

    private List<String> prepareReport( ) {
        return new ArrayList<String>();
    }

    private void saveReport() {
        String report = assembleReport();
        reportService.saveReport(ReportTypeName.MGI_ES_CELL, report);
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
                "MGI Gene Accession ID",
                "Genome Assembly",
                "Cassette",
                "Pipeline",
                "IKMC Project Name",
                "ES Cell Name",
                "ES Cell Parent",
                "ES Cell Allele Symbol",
                "ES Cell Mutation Type",
                "ES Cell Mutation Subtype",
                "Cassette Start",
                "Cassette End",
                "LoxP Start",
                "LoxP End",
                "Is Mixed"
        );

        String headerString =   headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;
    }
}
