package org.gentar.report.collection.mgi_modification_allele;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MgiModificationAlleleServiceImpl implements MgiModificationAlleleService{

    private final ReportServiceImpl reportService;

    private List<String> reportRows;

    public MgiModificationAlleleServiceImpl(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @Override
    public void generateMgiModificationAlleleReport() {
        reportRows = prepareReport();
        saveReport();
    }

    private List<String> prepareReport( ) {
        return new ArrayList<String>();
    }

    private void saveReport() {
        String report = assembleReport();
        reportService.saveReport(ReportTypeName.MGI_MODIFICATION, report);
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
                "Production Plan Colony Name",
                "Production Plan Colony Background Strain",
                "Production Work Unit",
                "Production Mutation Symbol",
                "ES Cell Allele Symbol",
                "ES Cell Allele Accession ID",
                "ES Cell Name",
                "ES Cell Parent",
                "Modification Plan Colony Name",
                "Excision Type",
                "Tat Cre",
                "Deleter Strain",
                "Modification Plan Colony Background Strain",
                "Modification Work Unit",
                "Modification Mutation Accession ID",
                "Modification Mutation Symbol"
        );

        String headerString =   headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;
    }

}
