package org.gentar.biology.targ_rep.targeting_vector;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.ArrayList;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;

public class TargetingVectorFieldsDescriptors {

    public static List<FieldDescriptor> getTargetingVectorFieldsDescriptions(String prefix) {
        List<FieldDescriptor> targetingVectorFieldsDescriptions = new ArrayList<>();
        addField(targetingVectorFieldsDescriptions, prefix + "id", "Public identifier for the Targeting Vector. READ ONLY");
        addField(targetingVectorFieldsDescriptions, prefix + "name", "Targeting vector fields name");
        addField(targetingVectorFieldsDescriptions, prefix + "intermediateVector", "Intermediate vector");
        addField(targetingVectorFieldsDescriptions, prefix + "reportToPublic", "Indicates if TargetingVector will be reported to public");
        addField(targetingVectorFieldsDescriptions, prefix + "ikmcProjectName", "IKMC project mame");
        addField(targetingVectorFieldsDescriptions, prefix + "mgiAlleleNamePrediction", "MGI Allele Name Prediction");
        addField(targetingVectorFieldsDescriptions, prefix + "productionCentreAutoUpdate", "Production Centre Auto Update");
        addField(targetingVectorFieldsDescriptions, prefix + "alleleTypePrediction", "Allele type prediction");
        addField(targetingVectorFieldsDescriptions, prefix + "ikmc_project", "IKMC Project");
        addField(targetingVectorFieldsDescriptions, prefix + "_links.self.href", "Targeting vektor Self  Link");
        addPipelineFields(prefix, targetingVectorFieldsDescriptions);
        addAlleleFields(prefix, targetingVectorFieldsDescriptions);
        addIkmcProjectFields(prefix, targetingVectorFieldsDescriptions);
        if (!prefix.isEmpty()) {
            addField(targetingVectorFieldsDescriptions, "_links.self.href",
                "Targeting Vector Link");
            addField(targetingVectorFieldsDescriptions, "page.size", "Page Size");
            addField(targetingVectorFieldsDescriptions, "page.totalElements", "Total Element");
            addField(targetingVectorFieldsDescriptions, "page.totalPages", "ToTal Page");
            addField(targetingVectorFieldsDescriptions, "page.number", "Page number");
        }

        return targetingVectorFieldsDescriptions;
    }

    private static void addPipelineFields(String prefix,
                                  List<FieldDescriptor> targetingVectorFieldsDescriptions) {
        addField(targetingVectorFieldsDescriptions, prefix + "pipeline.id", "id. READ ONLY");
        addField(targetingVectorFieldsDescriptions, prefix + "pipeline.name", "Pipeline name");
        addField(targetingVectorFieldsDescriptions, prefix + "pipeline.description",
            "Pipeline description.");
        addField(targetingVectorFieldsDescriptions, prefix + "pipeline.reportToPublic",
            "Indicates if pipeline will be reported to public");
        addField(targetingVectorFieldsDescriptions, prefix + "pipeline.geneTrap",
            "generic transcriptome analysis pipeline");
        addField(targetingVectorFieldsDescriptions, prefix + "pipeline._links.pipeline[].href", "Pipeline Self  Link");

    }

    private static void addIkmcProjectFields(String prefix,
                                          List<FieldDescriptor> targetingVectorFieldsDescriptions) {
        addField(targetingVectorFieldsDescriptions, prefix + "ikmc_project.id", "id. READ ONLY");
        addField(targetingVectorFieldsDescriptions, prefix + "ikmc_project.name", "Pipeline name");
        addField(targetingVectorFieldsDescriptions, prefix + "ikmc_project.status",
            "Pipeline description.");
        addField(targetingVectorFieldsDescriptions, prefix + "ikmc_project.pipeline_id",
            "Indicates if pipeline will be reported to public");
    }

    private static void addAlleleFields(String prefix,
                                  List<FieldDescriptor> targetingVectorFieldsDescriptions) {
        addField(targetingVectorFieldsDescriptions, prefix + "allele.id", "id. READ ONLY.");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.assembly", "Allele Assembly");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.chromosome", "Allele Chromosome");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.strand", "Allele Strand");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.mgi_accession_id", "MGI Accession id");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.cassette", "Allele Cassette");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.backbone", "Allele Backbone");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.project_design_id", "Allele Project Design Id");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.design_type.name", "Allele Design Type Name");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.subtype_description", "Allele Subtype Description");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.homology_arm_start", "Allele Homology Arm Start");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.homology_arm_end", "Allele Homology Arm End");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.cassette_start", "Allele Cassette Start");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.cassette_end", "Allele Cassette End");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.loxp_start", "Allele Loxp Start");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.loxp_end", "Allele Loxp End");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.cassette_type", "Allele Cassette Type");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.floxed_start_exon", "Allele Floxed Star Exon");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.floxed_end_exon", "Allele Floxed End Exon");
        addField(targetingVectorFieldsDescriptions, prefix + "allele.design_subtype.name", "Allele Design Subtype");
        addField(targetingVectorFieldsDescriptions, prefix + "allele._links.allele[].href", "Allele Self  Link");

    }

    private static void addField(List<FieldDescriptor> list, String name, String description) {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
