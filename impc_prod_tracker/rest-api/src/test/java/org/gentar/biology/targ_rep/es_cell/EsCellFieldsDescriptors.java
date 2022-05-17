package org.gentar.biology.targ_rep.es_cell;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.ArrayList;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;

public class EsCellFieldsDescriptors {

    public static List<FieldDescriptor> getEsCellFieldsDescriptions(String prefix) {
        List<FieldDescriptor> esCellFieldsDescriptions = new ArrayList<>();
        addField(esCellFieldsDescriptions, prefix + "id",
            "Public identifier for the EsCell. READ ONLY");
        addField(esCellFieldsDescriptions, prefix + "pipelineId",
            "Public identifier for the Pipeline. READ ONLY");
        addField(esCellFieldsDescriptions, prefix + "alleleId",
            "Public identifier for the Allele. READ ONLY");
        addField(esCellFieldsDescriptions, prefix + "name", "EsCell name");
        addField(esCellFieldsDescriptions, prefix + "targetingVectorId",
            "Public identifier for the Targeting Vector. READ ONLY");
        addField(esCellFieldsDescriptions, prefix + "parentalCellLine", "Parental Cell Line");
        addField(esCellFieldsDescriptions, prefix + "ikmcProjectId",
            "Public identifier for the IKMC Project. READ ONLY");
        addField(esCellFieldsDescriptions, prefix + "mgiAlleleId",
            "Public identifier for the MGI Allele Id. READ ONLY");
        addField(esCellFieldsDescriptions, prefix + "alleleSymbolSuperscript",
            "Allele Symbol Super Script");
        addField(esCellFieldsDescriptions, prefix + "comment", "Comment");
        addField(esCellFieldsDescriptions, prefix + "contact", "no information, always empty");
        addField(esCellFieldsDescriptions, prefix + "reportToPublic",
            "ndicates if EsCell will be reported to public");
        addField(esCellFieldsDescriptions, prefix + "productionQcFivePrimeScreen",
            "Production Qc Five Prime Screen");
        addField(esCellFieldsDescriptions, prefix + "productionQcLossOfAllele",
            "Production Qc Loss Of Allele");
        addField(esCellFieldsDescriptions, prefix + "productionQcLoxpScreen",
            "Production Qc Loxp Screen");
        addField(esCellFieldsDescriptions, prefix + "productionQcThreePrimeScreen",
            "Production Qc Three Prime Screen");
        addField(esCellFieldsDescriptions, prefix + "productionQcVectorIntegrity",
            "Production Qc Vector Integrity");
        addField(esCellFieldsDescriptions, prefix + "userQcComment", "User Qc Comment");
        addField(esCellFieldsDescriptions, prefix + "userQcFivePrimeCassetteIntegrity",
            "User Qc Five Prime Cassette Integrity");
        addField(esCellFieldsDescriptions, prefix + "userQcFivePrimeLrPcr",
            "User Qc Five Prime LrPcr");
        addField(esCellFieldsDescriptions, prefix + "userQcKaryotype", "User Qc Karyotype");
        addField(esCellFieldsDescriptions, prefix + "userQcLaczSrPcr", "User Qc Lacz Sr Pcr");
        addField(esCellFieldsDescriptions, prefix + "userQcLossOfWtAllele",
            "User Qc Loss Of Wt Allele");
        addField(esCellFieldsDescriptions, prefix + "userQcLoxpConfirmation",
            "User Qc Loxp Confirmation");
        addField(esCellFieldsDescriptions, prefix + "userQcMapTest", "User Qc Map Test");
        addField(esCellFieldsDescriptions, prefix + "userQcMutantSpecificSrPcr",
            "User Qc Mutant Specific Sr Pcr");
        addField(esCellFieldsDescriptions, prefix + "userQcNeoCountQpcr",
            "User Qc Neo Count Q pcr");
        addField(esCellFieldsDescriptions, prefix + "userQcNeoSrPcr", "User Qc Neo Sr Pcr");
        addField(esCellFieldsDescriptions, prefix + "userQcSouthernBlot", "User Qc Southern Blot");
        addField(esCellFieldsDescriptions, prefix + "userQcThreePrimeLrPcr",
            "user Qc Three Prime Lr Pcr");
        addField(esCellFieldsDescriptions, prefix + "userQcTvBackboneAssay",
            "user Qc Tv Backbone Assay");
        setDistributionQcList(prefix, esCellFieldsDescriptions);
        addField(esCellFieldsDescriptions, prefix + "_links.pipeline[].href",
            "Pipeline Self Link");
        addField(esCellFieldsDescriptions, prefix + "_links.targeting_vector[].href",
            "targeting Vector Self Link");
        addField(esCellFieldsDescriptions, prefix + "_links.allele[].href", "Allele Self Link");
        addField(esCellFieldsDescriptions, prefix + "_links.self.href", "EsCell Self Link");

        if (prefix.equals("_embedded.targrep_es_cells[].")) {
            addField(esCellFieldsDescriptions, "_links.self.href", "EsCell Link");
            addField(esCellFieldsDescriptions, "page.size", "Page Size");
            addField(esCellFieldsDescriptions, "page.totalElements", "Total Element");
            addField(esCellFieldsDescriptions, "page.totalPages", "ToTal Page");
            addField(esCellFieldsDescriptions, "page.number", "Page number");
        }
        return esCellFieldsDescriptions;
    }


    public static List<FieldDescriptor> getEsCellLegacyFieldsDescriptions(String prefix) {
        List<FieldDescriptor> esCellFieldsDescriptions = new ArrayList<>();
        addField(esCellFieldsDescriptions, prefix + "name",
            "Public identifier name for the EsCell.");
        addField(esCellFieldsDescriptions, prefix + "pipelineName",
            "Public identifier name for the Pipeline.");

        return esCellFieldsDescriptions;
    }

    private static void setDistributionQcList(String prefix,
                                              List<FieldDescriptor> esCellFieldsDescriptions) {
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].id",
            "Distribution Qc Id");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].karyotypeHigh",
            "Distribution Qc Karyotype High");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].karyotypeLow",
            "Distribution Qc Karyotype Low");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].copyNumber",
            "Distribution Qc Copy Number");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].fivePrimeLrPcr",
            "Distribution Qc Five Prime Lr Pcr");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].fivePrimeSrPcr",
            "Distribution Qc Five Prime Sr Pcr");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].thawing",
            "Distribution Qc Thawing");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].threePrimeLrPcr",
            "Distribution Qc Three Prime Lr Pcr");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].threePrimeSrPcr",
            "Distribution Qc Three Prime Sr Pcr");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].loa",
            "Distribution Loa");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].loxp",
            "Distribution Loxp");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].lacz",
            "Distribution Lacz");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].chr1",
            "Distribution Chr1");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].chr8a",
            "Distribution Chr8a");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].chr8b",
            "Distribution Chr8b");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].chr11a",
            "Distribution Chr11a");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].chr11b",
            "Distribution Chr11b");
        addField(esCellFieldsDescriptions, prefix + "targRepDistributionQcList[].chry",
            "Distribution Chry");
        addField(esCellFieldsDescriptions,
            prefix + "targRepDistributionQcList[].esCellDistributionCentre.id",
            "es Cell distribution id");
        addField(esCellFieldsDescriptions,
            prefix + "targRepDistributionQcList[].esCellDistributionCentre.name",
            "es Cell distribution name");
    }

    private static void addField(List<FieldDescriptor> list, String name, String description) {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
