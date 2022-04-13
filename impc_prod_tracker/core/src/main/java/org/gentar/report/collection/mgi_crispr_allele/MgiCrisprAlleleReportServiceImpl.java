package org.gentar.report.collection.mgi_crispr_allele;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportService;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyProjection;
import org.gentar.report.collection.mgi_crispr_allele.colony.MgiCrisprAlleleReportColonyService;
import org.gentar.report.collection.mgi_crispr_allele.genotype_primer.MgiCrisprAlleleReportGenotypePrimerProjection;
import org.gentar.report.collection.mgi_crispr_allele.guide.MgiCrisprAlleleReportGuideProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorProjection;
import org.gentar.report.collection.mgi_crispr_allele.mutation_characterization.MgiCrisprAlleleReportMutationCategorizationProjection;
import org.gentar.report.collection.mgi_crispr_allele.nuclease.MgiCrisprAlleleReportNucleaseProjection;
import org.gentar.report.collection.mgi_crispr_allele.outcome.MgiCrisprAlleleReportOutcomeMutationProjection;
import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;
import org.gentar.report.utils.genotype_primer.MgiGenotypePrimerFormatHelper;
import org.gentar.report.utils.guide.MgiGuideFormatHelper;
import org.gentar.report.utils.mutagenesis_donor.MgiMutagenesisDonorFormatHelper;
import org.gentar.report.utils.mutation_categorization.MgiMutationCategorizationFormatHelper;
import org.gentar.report.utils.nuclease.MgiNucleaseFormatHelper;
import org.gentar.report.utils.sequence.MgiMutationSequenceFormatHelper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MgiCrisprAlleleReportServiceImpl implements MgiCrisprAlleleReportService {

    private final MgiCrisprAlleleReportColonyService colonyReportService;
    private final ReportService reportService;

    private final MgiGuideFormatHelper mgiGuideFormatHelper;
    private final MgiNucleaseFormatHelper mgiNucleaseFormatHelper;
    private final MgiMutationSequenceFormatHelper mgiMutationSeqeunceFormatHelper;
    private final MgiMutationCategorizationFormatHelper mgiMutationCategorizationFormatHelper;
    private final MgiMutagenesisDonorFormatHelper mgiMutagenesisDonorFormatHelper;
    private final MgiGenotypePrimerFormatHelper mgiGenotypePrimerFormatHelper;

    private List<String> reportRows;
    private List<MgiCrisprAlleleReportColonyProjection> cp;
    private Map<Long, Set<MgiCrisprAlleleReportGuideProjection>> guideMap;
    private Map<Long, Set<MgiCrisprAlleleReportNucleaseProjection>> nucleaseMap;
    private Map<Long, Set<MgiCrisprAlleleReportMutagenesisDonorProjection>> mutagenesisDonorMap;
    private Map<Long, Set<MgiCrisprAlleleReportGenotypePrimerProjection>> genotypePrimerMap;
    private Map<Long, Set<MgiCrisprAlleleReportMutationSequenceProjection>> sequenceMap;
    private Map<Long, Set<MgiCrisprAlleleReportMutationCategorizationProjection>> categorizationMap;
    private Map<Long, MgiCrisprAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;

    public MgiCrisprAlleleReportServiceImpl(MgiCrisprAlleleReportColonyService colonyReportService,
                                            ReportService reportService,
                                            MgiGuideFormatHelper mgiGuideFormatHelper,
                                            MgiNucleaseFormatHelper mgiNucleaseFormatHelper,
                                            MgiMutationSequenceFormatHelper mgiMutationSeqeunceFormatHelper,
                                            MgiMutationCategorizationFormatHelper mgiMutationCategorizationFormatHelper,
                                            MgiMutagenesisDonorFormatHelper mgiMutagenesisDonorFormatHelper,
                                            MgiGenotypePrimerFormatHelper mgiGenotypePrimerFormatHelper) {
        this.colonyReportService = colonyReportService;
        this.reportService = reportService;
        this.mgiGuideFormatHelper = mgiGuideFormatHelper;
        this.mgiNucleaseFormatHelper = mgiNucleaseFormatHelper;
        this.mgiMutationSeqeunceFormatHelper = mgiMutationSeqeunceFormatHelper;
        this.mgiMutationCategorizationFormatHelper = mgiMutationCategorizationFormatHelper;
        this.mgiMutagenesisDonorFormatHelper = mgiMutagenesisDonorFormatHelper;
        this.mgiGenotypePrimerFormatHelper = mgiGenotypePrimerFormatHelper;
    }

    @Override
    public void generateMgiCrisprAlleleReport() {

        cp = colonyReportService.getAllColonyReportProjections();
        guideMap = colonyReportService.getGuideMap();
        nucleaseMap = colonyReportService.getNucleaseMap();
        mutagenesisDonorMap = colonyReportService.getMutagenesisDonorMap();
        genotypePrimerMap = colonyReportService.getGenotypePrimerMap();
        filteredOutcomeMutationMap = colonyReportService.getMutationMap();
        filteredMutationGeneMap = colonyReportService.getGeneMap();
        sequenceMap = colonyReportService.getMutationSequenceMap();
        categorizationMap = colonyReportService.getMutationCategorizationMap();

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

        Set<MgiCrisprAlleleReportMutationCategorizationProjection> categorizationProjections =
                categorizationMap.get(mutationProjection.getMutationId());

        Gene g = filteredMutationGeneMap.get(mutationProjection.getMutationId());
        String formattedMutationSymbol = checkMutationSymbol(mutationSymbol, g.getSymbol());

        Set<MgiCrisprAlleleReportGuideProjection> guideProjections = guideMap.get(x.getPlanId());
        Set<MgiCrisprAlleleReportNucleaseProjection> nucleaseProjections = nucleaseMap.get(x.getPlanId());
        Set<MgiCrisprAlleleReportMutagenesisDonorProjection> mutagenesisDonorProjections =
                mutagenesisDonorMap.get(x.getPlanId());
        Set<MgiCrisprAlleleReportGenotypePrimerProjection> genotypePrimerProjections =
                genotypePrimerMap.get(x.getPlanId());

        return g.getSymbol() + "\t" +
                g.getAccId() + "\t" +
                mgiMutagenesisDonorFormatHelper.formatMutagenesisDonorData(mutagenesisDonorProjections) + "\t" +
                mgiNucleaseFormatHelper.formatNucleaseData(nucleaseProjections) + "\t" +
                mgiGuideFormatHelper.formatGuideData(guideProjections) + "\t" +
                mgiMutationCategorizationFormatHelper.formatRepairMechanism(categorizationProjections) + "\t" +
                mgiGenotypePrimerFormatHelper.formatGenotypePrimerData(genotypePrimerProjections) + "\t" +
                x.getColonyName() + "\t" +
                x.getStrainName() + "\t" +
                genotypingComment + "\t" +
                x.getProductionWorkUnit() + "\t" +
                "endonuclease-mediated" + "\t" +
                mutationCategory + "\t" +
                mutationType + "\t" +
                mgiMutationCategorizationFormatHelper.formatAlleleCategory(categorizationProjections) + "\t" +
                mgiMutationSeqeunceFormatHelper.formatMutationSeqeunceData(mutationSequenceProjections) + "\t" +
                formattedMutationSymbol + "\t" +
                mgiAlleleAccId + "\t" +
                mutationDescription;
    }

    private String checkMutationSymbol(String mutationSymbol, String geneSymbol) {
        String formattedMutationSymbol = "";

        if ((geneSymbol != null) && (mutationSymbol != null)) {

            if (mutationSymbol.contains(geneSymbol) && mutationSymbol.contains("<") && (mutationSymbol.contains(">"))) {
                formattedMutationSymbol = mutationSymbol;
            } else {
                formattedMutationSymbol = geneSymbol + "<" + mutationSymbol + ">";
            }
        }
        return formattedMutationSymbol;

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
                "Mutagenesis Donors",
                "Nucleases",
                "Guides",
                "Repair Mechanism",
                "Genotype Primers",
                "Colony Name",
                "Colony Background Strain",
                "Colony Genotyping Comments",
                "Production Work Unit",
                "Mutation Class",
                "Mutation Type",
                "Mutation Subtype",
                "Mutation Category",
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
