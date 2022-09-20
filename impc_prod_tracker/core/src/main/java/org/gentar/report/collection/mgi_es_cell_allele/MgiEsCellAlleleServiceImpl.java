package org.gentar.report.collection.mgi_es_cell_allele;

import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_es_cell_allele.targ_rep.MgiESCellAlleleTargRepESCellCloneProjection;
import org.gentar.report.collection.mgi_es_cell_allele.targ_rep.MgiEsCellAlleleTargRepProjection;
import org.gentar.report.collection.mgi_es_cell_allele.targ_rep.MgiEsCellAlleleTargRepService;
import org.gentar.report.collection.mgi_modification_allele.modification_colony.MgiModificationAlleleReportColonyProjection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MgiEsCellAlleleServiceImpl implements MgiEsCellAlleleService{

    private final MgiEsCellAlleleTargRepService mgiEsCellAlleleTargRepService;
    private final ReportServiceImpl reportService;

    private List<String> reportRows;
    private List<MgiEsCellAlleleTargRepProjection> trp;
    private List<MgiESCellAlleleTargRepESCellCloneProjection> clones;
    private List<String> uniqueclones;

    public MgiEsCellAlleleServiceImpl(MgiEsCellAlleleTargRepService mgiEsCellAlleleTargRepService,
                                      ReportServiceImpl reportService) {
        this.mgiEsCellAlleleTargRepService = mgiEsCellAlleleTargRepService;
        this.reportService = reportService;
    }

    @Override
    public void generateMgiEsCellAlleleReport() {
          clones = mgiEsCellAlleleTargRepService.getTargRepClones();
          uniqueclones = clones.stream()
                  .map(x -> x.getEsCellClone())
                  .collect(Collectors.toSet()).stream()
                  .collect(Collectors.toList());

          //Split cloneNames into batches
          List<List<String>> groups = getBatches(uniqueclones, 1000);
          trp = new ArrayList<>();
          groups.forEach(groupIds -> mgiEsCellAlleleTargRepService.getSelectedTargRepProjections(groupIds)
                  .stream()
                  .forEach(trp::add));


//        trp = mgiEsCellAlleleTargRepService.getTargRepProjections();
        reportRows = prepareReport();
        saveReport();
    }

    private List<String> prepareReport( ) {
        return trp.stream()
                .map(x -> constructRow(x))
                .collect(Collectors.toList());
    }

    private String constructRow(MgiEsCellAlleleTargRepProjection x) {

        return x.getMgiAccessionId() + "\t" +
                x.getAssembly() + "\t" +
                x.getCassette() + "\t" +
                x.getPipeline() + "\t" +
                x.getIkmcProjectId() + "\t" +
                x.getEsCellClone() + "\t" +
                x.getParentCellLine() + "\t" +
                x.getAlleleSymbolSuperscript() + "\t" +
                x.getMutationType() + "\t" +
                x.getMutationSubtype() + "\t" +
                x.getCassetteStart() + "\t" +
                x.getCassetteEnd() + "\t" +
                x.getLoxpStart() + "\t" +
                x.getLoxpEnd() + "\t" +
                "";

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

    private static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        return IntStream.iterate(0, i -> i < collection.size(), i -> i + batchSize)
                .mapToObj(i -> collection.subList(i, Math.min(i + batchSize, collection.size())))
                .collect(Collectors.toList());
    }
}
