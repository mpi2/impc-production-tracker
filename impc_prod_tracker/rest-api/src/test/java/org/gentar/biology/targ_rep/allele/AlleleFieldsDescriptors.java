package org.gentar.biology.targ_rep.allele;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.ArrayList;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;

public class AlleleFieldsDescriptors {

    public static List<FieldDescriptor> getAlleleFieldsDescriptions(String prefix) {
        List<FieldDescriptor> alleleFieldsDescriptions = new ArrayList<>();
        addField(alleleFieldsDescriptions, prefix + "id",
            "Public identifier for the Allele. READ ONLY");
        addField(alleleFieldsDescriptions, prefix + "assembly", "Allele Assembly");
        addField(alleleFieldsDescriptions, prefix + "chromosome", "Allele Chromosome");
        addField(alleleFieldsDescriptions, prefix + "strand", "Allele Strand");
        addField(alleleFieldsDescriptions, prefix + "mgi_accession_id", "MGI Accession id");
        addField(alleleFieldsDescriptions, prefix + "cassette", "Allele Cassette");
        addField(alleleFieldsDescriptions, prefix + "backbone", "Allele Backbone");
        addField(alleleFieldsDescriptions, prefix + "project_design_id",
            "Allele Project Design Id");
        addField(alleleFieldsDescriptions, prefix + "design_type.name", "Allele Design Type Name");
        addField(alleleFieldsDescriptions, prefix + "subtype_description",
            "Allele Subtype Description");
        addField(alleleFieldsDescriptions, prefix + "homology_arm_start",
            "Allele Homology Arm Start");
        addField(alleleFieldsDescriptions, prefix + "homology_arm_end", "Allele Homology Arm End");
        addField(alleleFieldsDescriptions, prefix + "cassette_start", "Allele Cassette Start");
        addField(alleleFieldsDescriptions, prefix + "cassette_end", "Allele Cassette End");
        addField(alleleFieldsDescriptions, prefix + "loxp_start", "Allele Loxp Start");
        addField(alleleFieldsDescriptions, prefix + "loxp_end", "Allele Loxp End");
        addField(alleleFieldsDescriptions, prefix + "cassette_type", "Allele Cassette Type");
        addField(alleleFieldsDescriptions, prefix + "floxed_start_exon", "Allele Floxed Star Exon");
        addField(alleleFieldsDescriptions, prefix + "floxed_end_exon", "Allele Floxed End Exon");
        addField(alleleFieldsDescriptions, prefix + "design_subtype.name",
            "Allele Design Subtype name");
        addField(alleleFieldsDescriptions, prefix + "_links.self.href", "Allele Link");
        if (!prefix.equals("")) {
            addField(alleleFieldsDescriptions, "_links.self.href", "Allele Link");
            addField(alleleFieldsDescriptions, "page.size", "Page Size");
            addField(alleleFieldsDescriptions, "page.totalElements", "Total Element");
            addField(alleleFieldsDescriptions, "page.totalPages", "ToTal Page");
            addField(alleleFieldsDescriptions, "page.number", "Page number");
        }
        return alleleFieldsDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description) {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
