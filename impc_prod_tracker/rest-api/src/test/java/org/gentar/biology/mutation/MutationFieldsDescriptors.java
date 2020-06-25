package org.gentar.biology.mutation;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class MutationFieldDescriptors {
    
    public static List<FieldDescriptor> getMutationFieldDescriptions()
    {
        List<FieldDescriptor> mutationFieldDescriptions = new ArrayList<>();
        addField(mutationFieldDescriptions, "min", "Public identifier for the mutation.");
        addField(mutationFieldDescriptions, "mgiAlleleId", "Public identifier assigned by MGI for the allele. READ ONLY");
        addField(mutationFieldDescriptions, "mgiAlleleSymbol", "Allele symbol assigned. This field often only contains the allele superscript. The mgiAlleleSymbolRequiresConstruction field indicates whether or not the full allele symbol need construction from the information in this field and the associated gene information. In the cases of complex mutations that are associated with several genes the full allele symbol should be recorded in this field.");
        addField(mutationFieldDescriptions, "mgiAlleleSymbolWithoutImpcAbbreviation", "Used to indicate whether the allele symbol includes the IMPC abbreviation. READ ONLY");
        addField(mutationFieldDescriptions, "mgiAlleleSymbolRequiresConstruction", "Indicates if the allele symbol needs to be constructed from a superscript recorded in the mgiAlleleSymbol field and the associated gene information.");
        addField(mutationFieldDescriptions, "geneticMutationTypeName", "Formal classification of the mutation in genetic terms, or an indication that the mutation has not been tested genetically.");
        addField(mutationFieldDescriptions, "molecularMutationTypeName", "The type of the molecular lesion associated with the mutation.");
        addField(mutationFieldDescriptions, "imitsAllele", "The identifier assigned to the allele in iMits.");
        addField(mutationFieldDescriptions, "alleleConfirmed", "Indicates if the allele has been verified.");

        addField(
                mutationFieldDescriptions, "mutationQcResults[]", "Array containing the results of QC tests carried out on the mutation.");
        addField(
                mutationFieldDescriptions, "mutationQcResults[].qcTypeName", "Type of Mutation QC carried out.");
        addField(
                mutationFieldDescriptions, "mutationQcResults[].statusName", "Status of the Mutation QC.");

        addField(
                mutationFieldDescriptions, "mutationSequences[]", "Array containing the results of QC tests carried out on the mutation.");
        addField(
                mutationFieldDescriptions, "mutationSequences[].id", "Type of Mutation QC carried out.");
        addField(
                mutationFieldDescriptions, "mutationSequences[].index", "Status of the Mutation QC.");
        addField(
                mutationFieldDescriptions, "mutationSequences[].sequence", "Status of the Mutation QC.");

        addField(
                mutationFieldDescriptions, "_links.outcomes.href", "Outcomes associated with the mutation.");
        return mutationFieldDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
