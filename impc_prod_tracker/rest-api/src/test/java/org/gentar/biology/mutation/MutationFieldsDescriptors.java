package org.gentar.biology.mutation;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class MutationFieldsDescriptors
{

    public static List<FieldDescriptor> getMutationFieldsDescriptions()
    {
        List<FieldDescriptor> mutationFieldsDescriptions = new ArrayList<>();
        addField(mutationFieldsDescriptions, "id", "id. READ ONLY");
        addField(mutationFieldsDescriptions, "min", "Public identifier for the mutation. READ ONLY");
        addField(mutationFieldsDescriptions, "mgiAlleleId", "Public identifier assigned by MGI for the allele or aberration. READ ONLY");
        addField(mutationFieldsDescriptions, "symbol", "Allele or aberration symbol. SPECIFY ONLY WHEN CREATING");
        addField(mutationFieldsDescriptions, "mgiAlleleSymbolWithoutImpcAbbreviation", "[THIS FIELD IS DEPRECATED AND WILL BE REMOVED] Used to indicate whether the allele symbol includes the IMPC abbreviation. SPECIFY ONLY WHEN CREATING");
        addField(mutationFieldsDescriptions, "mgiAlleleSymbolRequiresConstruction", "[THIS FIELD IS DEPRECATED AND WILL BE REMOVED] Indicates if the allele symbol needs to be constructed from a superscript recorded in the mgiAlleleSymbol field and the associated gene information. SPECIFY ONLY WHEN CREATING");
        addField(mutationFieldsDescriptions, "geneticMutationTypeName", "Formal classification of the mutation in genetic terms, or an indication that the mutation has not been tested genetically.");
        addField(mutationFieldsDescriptions, "molecularMutationTypeName", "The type of the molecular lesion associated with the mutation.");

        addField(
            mutationFieldsDescriptions, "mutationQcResults[]", "Array containing the results of QC tests carried out on the mutation.");
        addField(
            mutationFieldsDescriptions, "mutationQcResults[].id", "Internal identifier. READ ONLY AND SUBJECT TO CHANGE.");
        addField(
            mutationFieldsDescriptions, "mutationQcResults[].qcTypeName", "Type of Mutation QC carried out.");
        addField(
            mutationFieldsDescriptions, "mutationQcResults[].statusName", "Status of the Mutation QC.");

        addField(
            mutationFieldsDescriptions, "mutationSequences[]", "Array containing a collection of sequences that characterise the mutation.");
        addField(
            mutationFieldsDescriptions, "mutationSequences[].id", "Internal identifier. READ ONLY AND SUBJECT TO CHANGE");
        addField(
            mutationFieldsDescriptions, "mutationSequences[].index", "Index used to order the sequences.");
        addField(
            mutationFieldsDescriptions, "mutationSequences[].sequence", "An object describing the sequence.");
        addField(
            mutationFieldsDescriptions, "mutationSequences[].sequence.id", "Internal identifier. READ ONLY AND SUBJECT TO CHANGE");
        addField(
            mutationFieldsDescriptions, "mutationSequences[].sequence.sequence", "A sequence in FASTA format.");
        addField(
            mutationFieldsDescriptions, "mutationSequences[].sequence.typeName", """
                        The type of sequence.

                        Possible values:

                        - complete

                        - partial

                        - 5_prime_end

                        - 3_prime_end""");
        addField(
            mutationFieldsDescriptions, "mutationSequences[].sequence.categoryName", """
                        The category assigned to the sequence.

                        Possible values:

                        - intention target sequence

                        - original starting sequence

                        - outcome sequence

                        A mutation should always be specified as 'outcome sequence'.""");
        addField(mutationFieldsDescriptions, "description", "Mutation description note.");
        addField(mutationFieldsDescriptions, "qcNote", "Mutation Qc note.");
        addField(
            mutationFieldsDescriptions, "mutationCategorizations[]", "A list classifying the mutation.");
        addField(
            mutationFieldsDescriptions, "mutationCategorizations[].name", "A label describing the mutation.");
        addField(
            mutationFieldsDescriptions, "mutationCategorizations[].description", "A longer description of this category.");
        addField(
            mutationFieldsDescriptions, "mutationCategorizations[].typeName", """
                        A broader classification of the description.

                        Possible values are:

                        - allele_category

                        - repair_mechanism""");
        addField(
            mutationFieldsDescriptions, "genes[]", "List of genes associated with the mutation.");
        addField(
            mutationFieldsDescriptions, "genes[].id", "An internal identifier for the gene. READ ONLY AND SUBJECT TO CHANGE");
        addField(
            mutationFieldsDescriptions, "genes[].name", "The name of the gene.");
        addField(
            mutationFieldsDescriptions, "genes[].symbol", "The valid symbol for the gene.");
        addField(
            mutationFieldsDescriptions, "genes[].speciesName", "The species related to the gene.");
        addField(
            mutationFieldsDescriptions, "genes[].externalLink", "An external link with more information about the gene.");
        addField(
            mutationFieldsDescriptions, "genes[].accessionId", "The accession identifier for the gene.");
        addField(mutationFieldsDescriptions, "_links.self.href", "Link to the mutation.");
        addField(
            mutationFieldsDescriptions, "_links.outcomes[].href", "Outcomes associated with the mutation.");

        addField(
                mutationFieldsDescriptions, "isDeletionCoordinatesUpdatedManually", "Is Deletion Coordinates Updated Manually.");
        addField(
                mutationFieldsDescriptions, "isMutationDeletionChecked", "Is Mutation Deletion Checked.");
        addField(
                mutationFieldsDescriptions, "insertionSequences", "Insertion Sequences.");
        addField(
                mutationFieldsDescriptions, "molecularMutationDeletions", "Molecular Mutatio Deletions.");
        addField(
                mutationFieldsDescriptions, "targetedExons", "Targeted Exons.");
        addField(
                mutationFieldsDescriptions, "canonicalTargetedExons", "Canonical Targeted Exons.");
        return mutationFieldsDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
