package org.gentar.biology.mutation;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class MutationFieldsDescriptors {
    
    public static List<FieldDescriptor> getMutationFieldsDescriptions()
    {
        List<FieldDescriptor> mutationFieldsDescriptions = new ArrayList<>();
        addField(mutationFieldsDescriptions, "min", "Public identifier for the mutation. READ ONLY");
        addField(mutationFieldsDescriptions, "mgiAlleleId", "Public identifier assigned by MGI for the allele. READ ONLY");
        addField(mutationFieldsDescriptions, "mgiAlleleSymbol", "Allele symbol assigned. This field often only contains the allele superscript. The mgiAlleleSymbolRequiresConstruction field indicates if the full allele symbol need construction from the information in this field and the associated gene information. In cases of complex mutations associated with several genes the full allele symbol should be recorded in this field. SPECIFY ONLY WHEN CREATING");
        addField(mutationFieldsDescriptions, "mgiAlleleSymbolWithoutImpcAbbreviation", "Used to indicate whether the allele symbol includes the IMPC abbreviation. SPECIFY ONLY WHEN CREATING");
        addField(mutationFieldsDescriptions, "mgiAlleleSymbolRequiresConstruction", "Indicates if the allele symbol needs to be constructed from a superscript recorded in the mgiAlleleSymbol field and the associated gene information. SPECIFY ONLY WHEN CREATING");
        addField(mutationFieldsDescriptions, "geneticMutationTypeName", "Formal classification of the mutation in genetic terms, or an indication that the mutation has not been tested genetically.");
        addField(mutationFieldsDescriptions, "molecularMutationTypeName", "The type of the molecular lesion associated with the mutation.");
        addField(mutationFieldsDescriptions, "imitsAllele", "The identifier assigned to the allele in iMits. READ ONLY");

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
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequence", "A sequence in FASTA format.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.typeName", "The type of sequence.\n\nPossible values:\n" +
                        "\n" +
                        "complete\n\n" +
                        "partial\n\n" +
                        "5_prime_end\n\n" +
                        "3_prime_end");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.categoryName", "The category assigned to the sequence.\n\n" +
                        "Possible values:\n\n" +
                        "intention target sequence\n\n" +
                        "original starting sequence\n\n" +
                        "outcome sequence\n\n" +
                        "A mutation should always be specified as 'outcome sequence'.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[]", "Sequence locations defined in a publicly available genome build associated with the sequence object.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].locationIndex", "Index used to order the locations.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location", "A location object.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location.chr", "The chromosome for the sequence location.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location.start", "The start of the sequence location.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location.stop", "The end of the sequence location.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location.strand", "The strand of the sequence location.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location.genomeBuild", "The genome build that defines the location.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location.strainName", "The name of the strain sequenced.");
        addField(
                mutationFieldsDescriptions, "mutationSequences[].sequence.sequenceLocations[].location.speciesName", "The species sequenced.");
        addField(
                mutationFieldsDescriptions, "mutationCategorizations[]", "A list classifying the mutation.");
        addField(
                mutationFieldsDescriptions, "mutationCategorizations[].name", "A label describing the mutation.");
        addField(
                mutationFieldsDescriptions, "mutationCategorizations[].description", "A longer description of this category.");
        addField(
                mutationFieldsDescriptions, "mutationCategorizations[].typeName", "A broader classification of the description.\n\n" +
                                                                                                    "Possible values are:\n\n" +
                                                                                                    "allele_category\n\n"+
                                                                                                    "repair_mechanism");
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

        addField(
                mutationFieldsDescriptions, "_links.outcomes.href", "Outcomes associated with the mutation.");
        return mutationFieldsDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
