package org.gentar.biology.mutation;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.mutation.genome_browser.GenomeBrowserCombinedProjection;
import org.gentar.biology.mutation.genome_browser.GenomeBrowserProjection;
import org.gentar.report.Report;
import org.gentar.report.ReportService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class MutationReportServiceImpl implements MutationReportService {

    private final MutationRepository mutationRepository;

    private final ReportService reportService;

    public MutationReportServiceImpl(MutationRepository mutationRepository, ReportService reportService) {
        this.mutationRepository = mutationRepository;
        this.reportService = reportService;
    }

    @Override
    public void getDeletionCoordinates(HttpServletResponse response) throws IOException {
        List<GenomeBrowserProjection> deletionCoordinates = mutationRepository.findAllDeletionCoordinates();
        formatGenomeBrowserProjectionReportText(response, deletionCoordinates, "Deletion_Coordinates");
    }

    @Override
    public void getTargetedExons(HttpServletResponse response) throws IOException {
        List<GenomeBrowserProjection> allTargetedExons = mutationRepository.findAllTargetedExons();
        formatGenomeBrowserProjectionReportText(response, allTargetedExons, "Targeted_Exons");
    }

    @Override
    public void getCanonicalTargetedExons(HttpServletResponse response) throws IOException {
        List<GenomeBrowserProjection> allCanonicalTargetedExons = mutationRepository.findAllCanonicalTargetedExons();
        formatGenomeBrowserProjectionReportText(response, allCanonicalTargetedExons, "Canonical_Targeted_Exons");
    }


    @Override
    public void getGenomeBrowserCombine(HttpServletResponse response, String workUnit) throws IOException {
        List<GenomeBrowserCombinedProjection> allGenomeBrowserCombinedProjections;
        if (workUnit == null || workUnit.isEmpty()) {
            allGenomeBrowserCombinedProjections = mutationRepository.findAllGenomeBrowserProjections();
        } else {
            allGenomeBrowserCombinedProjections = mutationRepository.findAllGenomeBrowserProjectionsByWorkuUnit(workUnit);
        }
        formatGenomeBrowserCombinedProjectionReportText(response, allGenomeBrowserCombinedProjections, "Genome_Browser_All_Report");
    }


    private void formatGenomeBrowserProjectionReportText(HttpServletResponse response,
                                                         List<GenomeBrowserProjection> genomeBrowserProjections, String reportTitle)

            throws IOException {
        StringBuilder reportText = new StringBuilder();

        reportText.append("Centre\tMin\tAllele Symbol\tGene Symbol\tFASTA\tUrl\n");


        for (GenomeBrowserProjection genomeBrowserProjection : genomeBrowserProjections) {
            String cleanedFasta = genomeBrowserProjection.getFasta().replace("\n", " ");
            reportText.append(genomeBrowserProjection.getCentre()).append("\t").append(genomeBrowserProjection.getMin()).append("\t").append(genomeBrowserProjection.getAlleleSymbol()).append("\t").append(genomeBrowserProjection.getGeneSymbol()).append("\t").append(cleanedFasta).append("\t").append(genomeBrowserProjection.getUrl()).append("\n");

        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
                .writeReport(response, reportTitle,
                        report);
    }

    private void formatGenomeBrowserCombinedProjectionReportText(HttpServletResponse response,
                                                                 List<GenomeBrowserCombinedProjection> genomeBrowserCombinedProjections, String reportTitle)

            throws IOException {
        StringBuilder reportText = new StringBuilder();

        reportText.append("Centre\tMin\tAllele Symbol\tGene Symbol\tDeletion Coordinates\tTargeted Exons\tCanonical Targeted Exons\tFASTA\tUrl\n");


        for (GenomeBrowserCombinedProjection genomeBrowserCombinedProjection : genomeBrowserCombinedProjections) {

            String deletionCoords = genomeBrowserCombinedProjection.getDeletionCoordinates();
            String targetedExons = genomeBrowserCombinedProjection.getTargetedExons();
            String canonicalExons = genomeBrowserCombinedProjection.getCanonicalTargetedExons();

            if (!(
                    (deletionCoords == null || deletionCoords.isEmpty()) &&
                            (targetedExons == null || targetedExons.isEmpty()) &&
                            (canonicalExons == null || canonicalExons.isEmpty())
            )) {
                String cleanedFasta = genomeBrowserCombinedProjection.getFasta().replace("\n", " ");
                reportText.append(genomeBrowserCombinedProjection.getCentre()).append("\t")
                        .append(genomeBrowserCombinedProjection.getMin()).append("\t")
                        .append(genomeBrowserCombinedProjection.getAlleleSymbol()).append("\t")
                        .append(genomeBrowserCombinedProjection.getGeneSymbol()).append("\t")
                        .append(deletionCoords == null ? "" : deletionCoords).append("\t")
                        .append(targetedExons == null ? "" : targetedExons).append("\t")
                        .append(canonicalExons == null ? "" : canonicalExons).append("\t")
                        .append(cleanedFasta).append("\t")
                        .append(genomeBrowserCombinedProjection.getUrl()).append("\n");
            }
        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
                .writeReport(response, reportTitle,
                        report);
    }


}


