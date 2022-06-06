package org.gentar.biology.targ_rep.pipeline;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.ArrayList;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;

public class PipelineFieldsDescriptors {

    public static List<FieldDescriptor> getPipelineFieldsDescriptions(String prefix) {
        List<FieldDescriptor> pipelineFieldsDescriptions = new ArrayList<>();
        addField(pipelineFieldsDescriptions, prefix + "id", "Public identifier for the Pipeline. READ ONLY");
        addField(pipelineFieldsDescriptions, prefix + "name", "Pipeline name");
        addField(pipelineFieldsDescriptions, prefix + "description", "Pipeline description.");
        addField(pipelineFieldsDescriptions, prefix + "reportToPublic",
            "Indicates if pipeline will be reported to public");
        addField(pipelineFieldsDescriptions, prefix + "geneTrap",
            "generic transcriptome analysis pipeline");
        addField(pipelineFieldsDescriptions, prefix + "_links.self.href", "Pipeline Link");
        if (!prefix.equals("")) {
            addField(pipelineFieldsDescriptions, "_links.self.href", "Pipeline Link");
            addField(pipelineFieldsDescriptions, "page.size", "Page Size");
            addField(pipelineFieldsDescriptions, "page.totalElements", "Total Element");
            addField(pipelineFieldsDescriptions, "page.totalPages", "ToTal Page");
            addField(pipelineFieldsDescriptions, "page.number", "Page number");
        }
        return pipelineFieldsDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description) {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
