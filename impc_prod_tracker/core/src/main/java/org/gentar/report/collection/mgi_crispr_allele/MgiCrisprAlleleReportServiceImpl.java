package org.gentar.report.collection.mgi_crispr_allele;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyProjection;
import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyServiceImpl;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;
import org.gentar.report.utils.guide.MgiGuideFormatHelperImpl;
import org.gentar.report.utils.nuclease.MgiNucleaseFormatHelperImpl;
import org.gentar.report.utils.sequence.MgiMutationSeqeunceFormatHelper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MgiCrisprAlleleReportServiceImpl implements MgiCrisprAlleleReportService {

    private final MgiCrisprAlleleReportColonyServiceImpl colonyReportService;
    private final ReportServiceImpl reportService;
    private final MgiGuideFormatHelperImpl mgiGuideFormatHelper;
    private final MgiNucleaseFormatHelperImpl mgiNucleaseFormatHelper;
    private final MgiMutationSeqeunceFormatHelper mgiMutationSeqeunceFormatHelper;

    private List<String> reportRows;
    private List<MgiCrisprAlleleReportColonyProjection> cp;
    private Map<Long, Set<MgiCrisprAlleleReportGuideProjection>> guideMap;
    private Map<Long, Set<MgiCrisprAlleleReportNucleaseProjection>> nucleaseMap;
    private Map<Long, Set<MgiCrisprAlleleReportMutationSequenceProjection>> sequenceMap;
    private Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;

    public MgiCrisprAlleleReportServiceImpl(MgiCrisprAlleleReportColonyServiceImpl colonyReportService,
                                            ReportServiceImpl reportService,
                                            MgiGuideFormatHelperImpl mgiGuideFormatHelper,
                                            MgiNucleaseFormatHelperImpl mgiNucleaseFormatHelper,
                                            MgiMutationSeqeunceFormatHelper mgiMutationSeqeunceFormatHelper) {
        this.colonyReportService = colonyReportService;
        this.reportService = reportService;
        this.mgiGuideFormatHelper = mgiGuideFormatHelper;
        this.mgiNucleaseFormatHelper = mgiNucleaseFormatHelper;
        this.mgiMutationSeqeunceFormatHelper = mgiMutationSeqeunceFormatHelper;
    }

    @Override
    public void generateMgiCrisprAlleleReport() {

        cp = colonyReportService.getAllColonyReportProjections();
        guideMap = colonyReportService.getGuideMap();
        nucleaseMap = colonyReportService.getNucleaseMap();
        filteredOutcomeMutationMap = colonyReportService.getMutationMap();
        filteredMutationGeneMap = colonyReportService.getGeneMap();
        sequenceMap = colonyReportService.getMutationSequenceMap();

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

        MgiCrisprAlleleReportOutcomeMutationProjection mutationProjection =
                filteredOutcomeMutationMap.get(x.getOutcomeId());

        String mutationSymbol = mutationProjection.getSymbol();
        String mutationMgiAlleleAccId = mutationProjection.getMgiAlleleAccId();
        String mgiAlleleAccId = mutationMgiAlleleAccId == null ? "" : mutationMgiAlleleAccId;
        String genotypingComment = x.getGenotypingComment() == null ? "" : '"' + x.getGenotypingComment() + '"';
        String mutationCategory = mutationProjection.getMutationCategory();
        String mutationType = mutationProjection.getMutationType();
        String mutationDescription =
                mutationProjection.getDescription() == null ? "" : '"' + mutationProjection.getDescription() + '"';

        Set<MgiCrisprAlleleReportMutationSequenceProjection> mutationSequenceProjections =
                sequenceMap.get(mutationProjection.getMutationId());

        Gene g = filteredMutationGeneMap.get(mutationProjection.getMutationId());

        Set<MgiCrisprAlleleReportGuideProjection> guideProjections = guideMap.get(x.getPlanId());
        Set<MgiCrisprAlleleReportNucleaseProjection> nucleaseProjections = nucleaseMap.get(x.getPlanId());

        return g.getSymbol() + "\t" +
                g.getAccId() + "\t" +
                "" + "\t" +
                mgiNucleaseFormatHelper.formatNucleaseData(nucleaseProjections) + "\t" +
                mgiGuideFormatHelper.formatGuideData(guideProjections) + "\t" +
                x.getColonyName() + "\t" +
                x.getStrainName() + "\t" +
                genotypingComment + "\t" +
                "IMPC" + "\t" +
                x.getProductionWorkUnit() + "\t" +
                "endonuclease-mediated" + "\t" +
                mutationCategory + "\t" +
                mutationType + "\t" +
                mgiMutationSeqeunceFormatHelper.formatMutationSeqeunceData(mutationSequenceProjections) + "\t" +
                mutationSymbol + "\t" +
                mgiAlleleAccId + "\t" +
                mutationDescription;
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
                "Nucleases",
                "Guides",
                "Colony Name",
                "Colony Background Strain",
                "Colony Genotyping Comments",
                "Project Name",
                "Production Work Unit",
                "Mutation Class",
                "Mutation Type",
                "Mutation Subtype",
                "Mutation Sequence",
                "Mutation Symbol",
                "Mutation MGI Accession ID",
                "Mutation Description"
        );

        String headerString = headers
                .stream()
                .collect(Collectors.joining("\t"));

        return headerString;

    }
}
