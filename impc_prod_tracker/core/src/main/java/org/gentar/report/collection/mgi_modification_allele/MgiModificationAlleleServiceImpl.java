package org.gentar.report.collection.mgi_modification_allele;

import org.gentar.biology.gene.Gene;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.ReportTypeName;
import org.gentar.report.collection.mgi_modification_allele.modification_colony.MgiModificationAlleleReportColonyProjection;
import org.gentar.report.collection.mgi_modification_allele.modification_colony.MgiModificationAlleleReportColonyService;
import org.gentar.report.collection.mgi_modification_allele.outcome.MgiModificationAlleleReportOutcomeMutationProjection;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MgiModificationAlleleServiceImpl implements MgiModificationAlleleService{

    private final MgiModificationAlleleReportColonyService colonyReportService;
    private final ReportServiceImpl reportService;

    private List<String> reportRows;
    private List<MgiModificationAlleleReportColonyProjection> cp;
    private Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> filteredOutcomeMutationMap;
    private Map<Long, MgiModificationAlleleReportOutcomeMutationProjection> filteredProductionOutcomeMutationMap;
    private Map<Long, Gene> filteredMutationGeneMap;
    private Map<Long, String> filteredAlleleCategoryMap;


    public MgiModificationAlleleServiceImpl(MgiModificationAlleleReportColonyService colonyReportService,
                                            ReportServiceImpl reportService)
    {
        this.colonyReportService = colonyReportService;
        this.reportService = reportService;
    }

    @Override
    public void generateMgiModificationAlleleReport() {
        cp = colonyReportService.getAllMgiModificationAlleleReportColonyProjections();
        filteredOutcomeMutationMap = colonyReportService.getMutationMap();
        filteredProductionOutcomeMutationMap = colonyReportService.getProductionMutationMap();
        filteredMutationGeneMap = colonyReportService.getGeneMap();
        filteredAlleleCategoryMap = colonyReportService.getAlleleCategoryMap();

        reportRows = prepareReport();
        saveReport();
    }

    private List<String> prepareReport( ) {
        List<MgiModificationAlleleReportColonyProjection> sortedEntries =
                cp.stream()
                        .filter(x -> filteredOutcomeMutationMap.containsKey(x.getModificationOutcomeId()))
                        .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getModificationOutcomeId()).getMutationId()))
                        .sorted(Comparator.comparing(x -> filteredMutationGeneMap.get(
                                        filteredOutcomeMutationMap.get(x.getModificationOutcomeId()).getMutationId()).getSymbol()
                                )
                        ).toList();

        return sortedEntries
                .stream()
                .map(this::constructRow)
                .collect(Collectors.toList());
    }

    private String constructRow(MgiModificationAlleleReportColonyProjection x) {

        MgiModificationAlleleReportOutcomeMutationProjection productionMutationProjection =
                filteredProductionOutcomeMutationMap.get(x.getProductionOutcomeId());
        String productionMutationSymbol = "";
        if (productionMutationProjection != null){
            productionMutationSymbol = productionMutationProjection.getSymbol() == null ? "" : productionMutationProjection.getSymbol();
        }
        String productionMutationMin = "";
        if (productionMutationProjection != null){
            productionMutationMin = productionMutationProjection.getMutationIdentificationNumber() == null ? "" : productionMutationProjection.getMutationIdentificationNumber();
        }

        MgiModificationAlleleReportOutcomeMutationProjection modificationMutationProjection =
                filteredOutcomeMutationMap.get(x.getModificationOutcomeId());
        String modificationMutationSymbol = modificationMutationProjection.getSymbol();
        String modificationMutationMgiAlleleAccId = modificationMutationProjection.getMgiAlleleAccId();
        String modificationMgiAlleleAccId = modificationMutationMgiAlleleAccId == null ? "" : modificationMutationMgiAlleleAccId;

        String esCellAlleleSymbol = x.getEsCellAlleleSymbol() == null ? "" : x.getEsCellAlleleSymbol();
        String esCellName = x.getEsCellName() == null ? "" : x.getEsCellName();
        String esMgiAlleleAccId = x.getEsCellAlleleAccessionId() == null ? "" : x.getEsCellAlleleAccessionId();
        String esParentCellName = x.getParentalEsCellName() == null ? "" : x.getParentalEsCellName();
        String deleterStrain = x.getDeleterStrainName() == null ? "" : x.getDeleterStrainName();

        Gene g = filteredMutationGeneMap.get(modificationMutationProjection.getMutationId());

        // The symbols should now be correct in the GenTaR database - or updated by the symbol updater service
        String productionFormattedMutationSymbol = productionMutationSymbol;

        // String esCellAlleleFormattedSymbol = checkMutationSymbol(esCellAlleleSymbol, g.getSymbol());
        // String modificationFormattedMutationSymbol = checkMutationSymbol(modificationMutationSymbol, g.getSymbol());
        // String productionFormattedMutationSymbol = checkMutationSymbol(productionMutationSymbol, g.getSymbol());
        // Additional line to test trigger of mirror build.

        String alleleCategory = "";
        if (productionMutationProjection != null) {
            alleleCategory = filteredAlleleCategoryMap.get(modificationMutationProjection.getMutationId());
        }


        return g.getSymbol() + "\t" +
                g.getAccId() + "\t" +
                x.getProductionColonyName() + "\t" +
                x.getProductionStrainName() + "\t" +
                x.getProductionWorkUnit() + "\t" +
                productionFormattedMutationSymbol + "\t" +
                esCellAlleleSymbol + "\t" +
                esCellName + "\t" +
                esMgiAlleleAccId + "\t" +
                esParentCellName + "\t" +
                x.getModificationColonyName() + "\t" +
                alleleCategory + "\t" +
                x.getTatCre() + "\t" +
                deleterStrain + "\t" +
                x.getModificationStrainName() + "\t" +
                x.getModificationWorkUnit() + "\t" +
                modificationMgiAlleleAccId + "\t" +
                modificationMutationSymbol + "\t" +
                productionMutationMin;
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
        reportService.saveReport(ReportTypeName.MGI_MODIFICATION, report);
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
                "Production Plan Colony Name",
                "Production Plan Colony Background Strain",
                "Production Work Unit",
                "Production Mutation Symbol",
                "ES Cell Allele Symbol",
                "ES Cell Name",
                "ES Cell Allele Accession ID",
                "ES Cell Parent",
                "Modification Plan Colony Name",
                "Excision Type",
                "Tat Cre",
                "Deleter Strain",
                "Modification Plan Colony Background Strain",
                "Modification Work Unit",
                "Modification Mutation Accession ID",
                "Modification Mutation Symbol",
                "GenTaR Mutation Identifier"
        );

        return String.join("\t", headers);
    }

}
