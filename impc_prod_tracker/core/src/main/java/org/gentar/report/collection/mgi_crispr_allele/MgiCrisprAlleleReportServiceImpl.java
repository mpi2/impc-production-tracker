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
import org.gentar.report.utils.stringencoding.Formatter;
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

    public void generateMgiSimpleCrisprAlleleReport() {

        cp = colonyReportService.getAllColonyReportProjections();
        filteredOutcomeMutationMap = colonyReportService.getMutationMap();
        filteredMutationGeneMap = colonyReportService.getGeneMap();

        reportRows = prepareSimpleReport();
        saveSimpleReport();
    }


    private List<String> prepareReport() {
        List<MgiCrisprAlleleReportColonyProjection> sortedEntries =
            cp.stream()
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

    private String constructRow(MgiCrisprAlleleReportColonyProjection x) {

        MgiCrisprAlleleReportOutcomeMutationProjection mutationProjection =
            filteredOutcomeMutationMap.get(x.getOutcomeId());

        String mutationSymbol = mutationProjection.getSymbol();
        String mutationMgiAlleleAccId = mutationProjection.getMgiAlleleAccId();
        String mgiAlleleAccId = mutationMgiAlleleAccId == null ? "" : mutationMgiAlleleAccId;
        String genotypingComment = x.getGenotypingComment() == null ? "" : Formatter.clean(x.getGenotypingComment());
        String mutationCategory = mutationProjection.getMutationCategory();
        String mutationType = mutationProjection.getMutationType();
        String mutationDescription =
            mutationProjection.getDescription() == null ? "" : Formatter.clean(mutationProjection.getDescription());

        Set<MgiCrisprAlleleReportMutationSequenceProjection> mutationSequenceProjections =
            sequenceMap.get(mutationProjection.getMutationId());

        Set<MgiCrisprAlleleReportMutationCategorizationProjection> categorizationProjections =
            categorizationMap.get(mutationProjection.getMutationId());

        String gentarMutationIdentifier = mutationProjection.getMutationMin();

        Gene g = filteredMutationGeneMap.get(mutationProjection.getMutationId());
        // See below - simple report - for why this is no longer needed
        // String formattedMutationSymbol = checkMutationSymbol(mutationSymbol, g.getSymbol());

        Set<MgiCrisprAlleleReportGuideProjection> guideProjections = guideMap.get(x.getPlanId());
        Set<MgiCrisprAlleleReportNucleaseProjection> nucleaseProjections = nucleaseMap.get(x.getPlanId());
        Set<MgiCrisprAlleleReportMutagenesisDonorProjection> mutagenesisDonorProjections =
            mutagenesisDonorMap.get(x.getPlanId());
        Set<MgiCrisprAlleleReportGenotypePrimerProjection> genotypePrimerProjections =
            genotypePrimerMap.get(x.getPlanId());

//        if (g.getSymbol().equals("Zmynd19")){
//            System.out.println(mutationProjection.getDescription());
//            System.out.println(mutationDescription);
//        }

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
            gentarMutationIdentifier + "\t" +
            mgiMutationSeqeunceFormatHelper.formatMutationSeqeunceData(mutationSequenceProjections) + "\t" +
                mutationSymbol + "\t" +
            mgiAlleleAccId + "\t" +
            mutationDescription;
    }


    private List<String> prepareSimpleReport() {
        List<MgiCrisprAlleleReportColonyProjection> sortedEntries =
            cp.stream()
                .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
                .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()))
                .sorted(Comparator.comparing(x -> filteredMutationGeneMap.get(
                    filteredOutcomeMutationMap.get(x.getOutcomeId()).getMutationId()).getSymbol()
                    )
                ).toList();

        return sortedEntries
            .stream()
            .map(this::constructSimpleReportRow)
            .collect(Collectors.toList());
    }

    private String constructSimpleReportRow(MgiCrisprAlleleReportColonyProjection x) {

        MgiCrisprAlleleReportOutcomeMutationProjection mutationProjection =
            filteredOutcomeMutationMap.get(x.getOutcomeId());

        String mutationSymbol = mutationProjection.getSymbol();
        String mutationMgiAlleleAccId = mutationProjection.getMgiAlleleAccId();
        String mgiAlleleAccId = mutationMgiAlleleAccId == null ? "" : mutationMgiAlleleAccId;
        String mutationCategory = mutationProjection.getMutationCategory();
        String mutationType = mutationProjection.getMutationType();
        String gentarMutationIdentifier = mutationProjection.getMutationMin();


        Gene g = filteredMutationGeneMap.get(mutationProjection.getMutationId());
        //
        // No longer needed - mutation symbols cleaned in GenTaR DB
        // - all should have gene symbols.
        // Issue more likely to be having wrong gene symbol, which is addressed by the symbol updater
        //
        // String formattedMutationSymbol = checkMutationSymbol(mutationSymbol, g.getSymbol());


        return g.getSymbol() + "\t" +
            g.getAccId() + "\t" +
            x.getColonyName() + "\t" +
            x.getStrainName() + "\t" +
            "endonuclease-mediated" + "\t" +
            mutationCategory + "\t" +
            mutationType + "\t" +
                mutationSymbol + "\t" +
            mgiAlleleAccId + "\t" +
            gentarMutationIdentifier
        ;
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
        String report = String.join("\n", reportRows);

        return String.join("\n", header, report);

    }

    private void saveSimpleReport() {

        String report = assembleSimpleReport();
        reportService.saveReport(ReportTypeName.MGI_INITIAL_CRISPR, report);
    }


    private String assembleSimpleReport() {

        String header = generateSimpleReportHeaders();
        String report = String.join("\n", reportRows);

        return String.join("\n", header, report);

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
            "GenTaR Mutation ID",
            "Mutation Sequence",
            "Mutation Symbol",
            "Mutation MGI Accession ID",
            "Mutation Description"
        );

        return String.join("\t", headers);

    }

    private String generateSimpleReportHeaders() {
        List<String> headers = Arrays.asList(
            "Gene Symbol",
            "Gene MGI Accession ID",
            "Colony Name",
            "Colony Background Strain",
            "Mutation Class",
            "Mutation Type",
            "Mutation Subtype",
            "Mutation Symbol",
            "Mutation MGI Accession ID",
            "GenTaR Mutation ID"

        );

        return String.join("\t", headers);
    }
}
