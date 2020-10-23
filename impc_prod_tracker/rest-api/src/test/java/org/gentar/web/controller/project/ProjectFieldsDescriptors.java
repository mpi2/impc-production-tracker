package org.gentar.web.controller.project;

import org.springframework.restdocs.payload.FieldDescriptor;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ProjectFieldsDescriptors
{
    public static List<FieldDescriptor> getResponseFieldDescriptions()
    {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        descriptors.add(getTpnDescriptor());
        descriptors.addAll(getSharedFieldDescriptions());
        descriptors.addAll(getIntentionsFields(true));
        descriptors.addAll(getOrthologsFields());
        descriptors.addAll(getConsortiaFields(true));
        descriptors.addAll(getLinksFieldsDescriptions());
        descriptors.add(getSpeciesDescriptor());
        addField(
            descriptors,
            "assignmentStatusName",
            "Assignment Status for the project. It could be marked as in conflict if existing " +
                "projects are working in the same gene. Read only.");
        addField(
            descriptors,
            "summaryStatusName",
            "A status summarising the global status based on the statuses of the plans in the " +
                "project. Read only.");
        addField(
            descriptors,
            "reactivationDate",
            "Date, represented as string, on which the project was activated again (assignment " +
                "Status changed from inactive). Example: \"2020-09-24T00:00:05\". Read only.");
        addField(
            descriptors,
            "reactivationDate",
            "Date, represented as string, on which the project was activated again (assignment " +
                "Status changed from inactive). Example: \"2020-09-24T00:00:05\". Read only.");
        addField(descriptors, "relatedWorkUnitNames", "Work units associated with the project.");
        addField(descriptors, "relatedWorkGroupNames", "Work groups associated with the project.");
        addField(
            descriptors,
            "assignmentStatusStamps",
            "Stamps for the changes of Assignment Status. Read only.");
        return descriptors;
    }

    public static List<FieldDescriptor> getCreationFieldDescriptions()
    {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        descriptors.addAll(getSharedFieldDescriptions());
        descriptors.addAll(getIntentionsFields(false));
        descriptors.add(getSpeciesDescriptor());
        addField(
            descriptors,
            "planDetails",
            "Plan with minimum information to be created alongside with the project.");
        addField(descriptors, "planDetails.funderNames", "Name of the funders in for the plan.");
        addField(descriptors, "planDetails.workUnitName", "Work unit of the plan.");
        addField(descriptors, "planDetails.workGroupName", "Work group of the plan.");
        addField(descriptors, "planDetails.comment", "Comment in the plan.");
        addField(
            descriptors,
            "planDetails.attemptTypeName",
            "Attempt type name.");
        addField(
            descriptors,
            "planDetails.typeName",
            "Plan type name.");
        descriptors.addAll(getConsortiaFields(false));
        return descriptors;
    }

    public static List<FieldDescriptor> getUpdateFieldDescriptions()
    {
        List<FieldDescriptor> descriptors = ProjectFieldsDescriptors.getSharedFieldDescriptions();
        descriptors.add(getTpnDescriptor());
        return descriptors;
    }

    public static FieldDescriptor getTpnDescriptor()
    {
        return fieldWithPath("tpn")
            .description("Public identifier for the project. " +
                "Not editable, just for reference in the payload.").optional();
    }

    public static List<FieldDescriptor> getSharedFieldDescriptions()
    {
        List<FieldDescriptor> sharedFieldDescriptions = new ArrayList<>();
        addField(sharedFieldDescriptions, "recovery", "[WIP]");
        addField(sharedFieldDescriptions, "comment", "Comment on this project.");
        addField(
            sharedFieldDescriptions,
            "externalReference",
            "External reference for the project. It can be an identifier in the internal systems of " +
                "the centres.");
        addField(
            sharedFieldDescriptions,
            "privacyName",
            "Privacy level for the project (public, protected or restricted).");
        return sharedFieldDescriptions;
    }

    private static List<FieldDescriptor> getConsortiaFields(boolean includeId)
    {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        addField(descriptors, "consortia", "Consortia associated with the project.");
        if (includeId)
        {
            addField(descriptors, "consortia[].id", "Internal record id.");
        }
        addField(descriptors, "consortia[].consortiumName", "Name of the consortium.");
        return descriptors;
    }

    private static FieldDescriptor getSpeciesDescriptor()
    {
        return fieldWithPath("speciesNames")
            .description("Species associated with the project.").optional();
    }

    private static List<FieldDescriptor> getIntentionsFields(boolean includeIds)
    {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        addField(descriptors, "projectIntentions", "Intentions for the project.");
        addField(
            descriptors,
            "projectIntentions[].molecularMutationTypeName",
            "Name of the molecular mutation.");
        addField(
            descriptors,
            "projectIntentions[].mutationCategorizations",
            "Mutation categorizations linked to the project intention.");
        addField(
            descriptors,
            "projectIntentions[].mutationCategorizations[].name",
            "Name of type of the mutation categorization.");
        addField(
            descriptors,
            "projectIntentions[].mutationCategorizations[].description",
            "Description of the mutation categorization.");
        addField(
            descriptors,
            "projectIntentions[].mutationCategorizations[].typeName",
            "Name of type of the mutation categorization." +
                "Possible values allele_category or repair_mechanism.");
        descriptors.addAll(getIntentionByGeneFields());
        descriptors.addAll(getIntentionsBySequenceFields(includeIds));
        return descriptors;
    }

    private static List<FieldDescriptor> getIntentionByGeneFields()
    {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        addField(
            descriptors, "projectIntentions[].intentionByGene", "Gene in the intention.");
        addField(
            descriptors, "projectIntentions[].intentionByGene.gene", "Gene information.");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.gene.id",
            "Internal id of the gene in GenTaR.");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.gene.name",
            "Name of the gene.");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.gene.symbol",
            "Symbol of the gene.");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.gene.externalLink",
            "External link for the gene.");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.gene.accessionId",
            "Accession id for the gene, e.g MGI.");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.gene.speciesName",
            "Species associated with the gene.");
        return descriptors;
    }

    private static List<FieldDescriptor> getIntentionsBySequenceFields(boolean includeIds)
    {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        addField(
            descriptors, "projectIntentions[].intentionsBySequences", "Sequences in the intention.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].index",
            "Index of the sequence");
        addField(
            descriptors, "projectIntentions[].intentionsBySequences[].sequence", "Sequence information.");
        if (includeIds)
        {
            addField(
                descriptors,
                "projectIntentions[].intentionsBySequences[].sequence.id",
                "Internal record id.");
        }
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequence",
            "Sequence value.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.typeName",
            "Type of the sequence. Currently \"complete,partial\", \"5_prime_end\" or \"3_prime_end\"");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.categoryName",
            "Type of the sequence. Currently \"intention target sequence\", \"original starting " +
                "sequence\" or \"outcome sequence\" (the last one only valid for outcomes).");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations",
            "Information about the locations and their order in the sequence.");
        if (includeIds)
        {
            addField(
                descriptors,
                "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].id",
                "Internal record id.");
        }
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].locationIndex",
            "Index of the location (order).");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location",
            "Location information.");
        if (includeIds)
        {
            addField(
                descriptors,
                "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.id",
                "Internal record id.");
        }
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.chr",
            "Chromosome of the location.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.start",
            "Start position.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.stop",
            "Stop position.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.strand",
            "Strand of the location.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.genomeBuild",
            "Genome build used as reference.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.speciesName",
            "Name of the species.");
        addField(
            descriptors,
            "projectIntentions[].intentionsBySequences[].sequence.sequenceLocations[].location.strainName",
            "Name of the strain.");
        return descriptors;
    }

    private static List<FieldDescriptor> getOrthologsFields()
    {
        String symbolDescription =
            "Human gene symbol representing the predicted ortholog of the mouse gene.";
        String categoryDescription = "Category assigned to the ortholog. Possible values: LOW, " +
            "MODERATE and GOOD according to the support count. The LOW category indicates the " +
            "ortholog prediction has a support count of less than 5. The MODERATE category " +
            "indicates the prediction has a support count of between 5 and 8, and the GOOD category " +
            "indicates the support count is between 9 and 12";
        String supportDescription =
            "Services that predict this human gene to the ortholog of the mouse gene.";
        String supportCount =
            "Counter of how many services support this ortholog designation.";
        String linkDescription = "URL of the HGNC entry for this gene.";

        List<FieldDescriptor> descriptors = new ArrayList<>();
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.bestOrthologs[]",
            "A list of best orthologs for the gene (Support count > 4).");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.allOrthologs[]",
            "A list of all orthologs for the gene.");
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.bestOrthologs[].symbol",
            symbolDescription);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.bestOrthologs[].category",
            categoryDescription);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.bestOrthologs[].support",
            supportDescription);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.bestOrthologs[].supportCount",
            supportCount);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.bestOrthologs[].link",
            linkDescription);

        addField(
            descriptors,
            "projectIntentions[].intentionByGene.allOrthologs[].symbol",
            symbolDescription);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.allOrthologs[].category",
            categoryDescription);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.allOrthologs[].support",
            supportDescription);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.allOrthologs[].supportCount",
            supportCount);
        addField(
            descriptors,
            "projectIntentions[].intentionByGene.allOrthologs[].link",
            linkDescription);
        return descriptors;
    }

    private static List<FieldDescriptor> getLinksFieldsDescriptions()
    {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        addField(descriptors, "_links", "Links for project.");
        addField(descriptors, "_links.self.href", "Link to the project.");
        addField(descriptors, "_links.productionPlans", "Links to production plans.");
        addField(descriptors, "_links.productionPlans[].href", "Link to a specific production plan.");
        addField(descriptors, "_links.phenotypingPlans", "Links to phenotyping plans.");
        addField(descriptors, "_links.phenotypingPlans[].href", "Link to a specific phenotyping plan.");
        return descriptors;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
