package org.gentar.biology.mutation;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.mutation.genome_browser.DeletionCoordinatesProjection;
import org.gentar.biology.mutation.genome_browser.GenomeBrowserCombinedProjection;
import org.gentar.biology.mutation.genome_browser.SerializedGuideProjection;
import org.gentar.biology.mutation.genome_browser.TargetedExonsProjection;
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
        List<DeletionCoordinatesProjection> deletionCoordinates = mutationRepository.findAllDeletionCoordinates();
        formatDeletionCoordinatesReportText(response, deletionCoordinates, "Deletion_Coordinates");
    }

    @Override
    public void getTargetedExons(HttpServletResponse response) throws IOException {
        List<TargetedExonsProjection> allTargetedExons = mutationRepository.findAllTargetedExons();
        formatTargetedExonsReportText(response, allTargetedExons, "Targeted_Exons");
    }

    @Override
    public void getCanonicalTargetedExons(HttpServletResponse response) throws IOException {
        List<TargetedExonsProjection> allCanonicalTargetedExons = mutationRepository.findAllCanonicalTargetedExons();
        formatTargetedExonsReportText(response, allCanonicalTargetedExons, "Canonical_Targeted_Exons");
    }


    @Override
    public void getGenomeBrowserCombine(HttpServletResponse response, String workUnit) throws IOException {
        List<GenomeBrowserCombinedProjection> allGenomeBrowserCombinedProjections;
        if (workUnit == null || workUnit.isEmpty() || workUnit.equals("undefined")) {
            allGenomeBrowserCombinedProjections = mutationRepository.findAllGenomeBrowserProjections();
        } else {
            allGenomeBrowserCombinedProjections = mutationRepository.findAllGenomeBrowserProjectionsByWorkuUnit(workUnit);
        }
        formatGenomeBrowserCombinedProjectionReportText(response, allGenomeBrowserCombinedProjections, "Crispr_Alleles_Report");
    }

    @Override
    public List<SerializedGuideProjection> getSerializedGuides(HttpServletResponse response) throws IOException {
        return mutationRepository.findAllSerializedGuides();
    }


    private void formatDeletionCoordinatesReportText(HttpServletResponse response,
                                                         List<DeletionCoordinatesProjection> deletionCoordinatesProjections, String reportTitle)

            throws IOException {
        StringBuilder reportText = new StringBuilder();

        reportText.append("Centre\tMin\tAllele Symbol\tGene Symbol\tDeletion Coordinates\tFASTA\tUrl\n");


        for (DeletionCoordinatesProjection deletionCoordinatesProjection : deletionCoordinatesProjections) {
            String cleanedFasta = deletionCoordinatesProjection.getFasta().replace("\n", " ");
            reportText.append(deletionCoordinatesProjection.getCentre()).append("\t").append(deletionCoordinatesProjection.getMin()).append("\t").append(deletionCoordinatesProjection.getAlleleSymbol()).append("\t").append(deletionCoordinatesProjection.getGeneSymbol()).append("\t").append(deletionCoordinatesProjection.getDeletionCoordinates()).append("\t").append(cleanedFasta).append("\t").append(deletionCoordinatesProjection.getUrl()).append("\n");

        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
                .writeReport(response, reportTitle,
                        report);
    }

    private void formatTargetedExonsReportText(HttpServletResponse response,
                                                     List<TargetedExonsProjection> targetedExonsProjections, String reportTitle)

            throws IOException {
        StringBuilder reportText = new StringBuilder();

        reportText.append("Centre\tMin\tAllele Symbol\tGene Symbol\tExons\tFASTA\tUrl\n");


        for (TargetedExonsProjection targetedExonsProjection : targetedExonsProjections) {
            String cleanedFasta = targetedExonsProjection.getFasta().replace("\n", " ");
            reportText.append(targetedExonsProjection.getCentre()).append("\t").append(targetedExonsProjection.getMin()).append("\t").append(targetedExonsProjection.getAlleleSymbol()).append("\t").append(targetedExonsProjection.getGeneSymbol()).append("\t").append(targetedExonsProjection.getExons()).append("\t").append(cleanedFasta).append("\t").append(targetedExonsProjection.getUrl()).append("\n");

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


