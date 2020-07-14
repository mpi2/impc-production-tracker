package org.gentar.biology.plan;

import org.springframework.restdocs.payload.FieldDescriptor;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class PlanFieldsDescriptors
{
    public static List<FieldDescriptor> getSharedFieldDescriptions()
    {
        List<FieldDescriptor> sharedFieldDescriptions = new ArrayList<>();
        addField(sharedFieldDescriptions,"pin", "Public identifier for the plan.");
        addField(sharedFieldDescriptions, "tpn", "Public identifier for the project.");
        addField(sharedFieldDescriptions, "typeName", "Plan type.");
        addField(sharedFieldDescriptions, "attemptTypeName", "Attempt type.");
        addField(sharedFieldDescriptions, "comment", "Comment for the plan");
        addField(sharedFieldDescriptions, "statusName", "Current status of the plan.");
        addField(sharedFieldDescriptions, "statusDates[]", "Stamps of the statuses the plan has gone through.");
        addField(
            sharedFieldDescriptions,
            "summaryStatusName",
            "A status summarising the global status based on the statuses of the children of the plan.");
        addField(
            sharedFieldDescriptions,
            "summaryStatusDates[]",
            "Stamps of the statuses summaries the plan has gone through.");
        addField(sharedFieldDescriptions, "funderNames", "Funders for the plan.");
        addField(sharedFieldDescriptions, "workUnitName", "Work unit for the plan.");
        addField(sharedFieldDescriptions, "workGroupName", "Work group for the plan.");
        addField(
            sharedFieldDescriptions,
            "productsAvailableForGeneralPublic", "Products available for general public.");
        addField(
            sharedFieldDescriptions,
            "statusTransition",
            "Information about the current state in the state machine for the plan.");
        addField(sharedFieldDescriptions, "statusTransition.currentStatus", "Current status in the plan");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[]",
            "Transitions in the state machine given the current state.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].action",
            "Action or transition's name.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].description",
            "Transition's description.");
        addField(sharedFieldDescriptions,
            "statusTransition.transitions[].triggeredByUser",
            "Indicates whether the transition is executed by the user or by the system.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].available",
            "Indicates if the transition can be executed at the moment.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].note",
            "Additional explanation about the availability to execute the transition.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].nextStatus",
            "Next status that the plan will have if the transition is executed.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.actionToExecute",
            "Name of the transition (action) to execute.");
        return sharedFieldDescriptions;
    }

    public static List<FieldDescriptor> getCrisprFieldDescriptors()
    {
        List<FieldDescriptor> crisprFields = new ArrayList<>();

        addField(crisprFields, "crisprAttempt", "Information about the Crispr attempt.");
        addField(crisprFields, "crisprAttempt.miDate", "Micro-injection date.");
        addField(
            crisprFields,
            "crisprAttempt.experimental",
            "Whether the attempt is experimental or not.");
        addField(crisprFields, "crisprAttempt.comment", "Crispt attempt comment.");
        addField(
            crisprFields, "crisprAttempt.mutagenesisExternalRef", "Mutagenesis external reference.");
        addField(crisprFields, "crisprAttempt.imitsMiAttemptId", "Id in iMits for this record.");
        addField(
            crisprFields, "crisprAttempt.attemptExternalRef", "External reference for the attempt.");

        addField(
            crisprFields, "crisprAttempt.totalEmbryosInjected", "Total embryos injected.");
        addField(
            crisprFields, "crisprAttempt.totalEmbryosSurvived", "Total embryos survived.");
        addField(
            crisprFields, "crisprAttempt.embryo2Cell", "Number of embryos at the two cell stage.");
        addField(
            crisprFields, "crisprAttempt.strainInjectedName", "Name of the strain injected.");

        // Nucleases
        addField(
            crisprFields,
            "crisprAttempt.nucleases",
            "Information about the nucleases used in the attempt");
        addField(crisprFields, "crisprAttempt.nucleases[].id", "Id of the record in the system.");
        addField(crisprFields, "crisprAttempt.nucleases[].typeName", "Name of the type of nuclease.");
        addField(
            crisprFields,
            "crisprAttempt.nucleases[].concentration",
            "Concentration of the nuclease.");
        addField(crisprFields, "crisprAttempt.nucleases[].className", "Name of the class of nuclease. [protein or mRNA].");

        // Guides
        addField(
            crisprFields, "crisprAttempt.guides", "Information about the guides used in the attempt");
        addField(crisprFields, "crisprAttempt.guides[].id", "Id of the record in the system.");
        addField(crisprFields, "crisprAttempt.guides[].chr", "Chromosome in the guide.");
        addField(crisprFields, "crisprAttempt.guides[].start", "Start");
        addField(crisprFields, "crisprAttempt.guides[].stop", "Stop.");
        addField(
            crisprFields,
            "crisprAttempt.guides[].grnaConcentration",
            "gRNA concentration in the guide.");
        addField(crisprFields, "crisprAttempt.guides[].sequence", "sequence in the guide.");
        addField(
            crisprFields,
            "crisprAttempt.guides[].truncatedGuide",
            "Whether or not the guide is truncated.");
        addField(crisprFields, "crisprAttempt.guides[].strand", "Strand of the guide.");
        addField(crisprFields, "crisprAttempt.guides[].genomeBuild", "Genome build.");
        addField(crisprFields, "crisprAttempt.guides[].pam3", "PAM-3");
        addField(crisprFields, "crisprAttempt.guides[].pam5", "PAM-5");
        addField(crisprFields, "crisprAttempt.guides[].protospacerSequence", "Protospacer sequence");

        // Mutagenesis Donors
        addField(
            crisprFields,
            "crisprAttempt.mutagenesisDonors",
            "Information about the mutagenesis donors.");
        addField(
            crisprFields, "crisprAttempt.mutagenesisDonors[].id", "Id of the record in the system.");
        addField(
            crisprFields,
            "crisprAttempt.mutagenesisDonors[].concentration",
            "Mutagenesis donor's concentration.");
        addField(crisprFields, "crisprAttempt.mutagenesisDonors[].vectorName", "Vector's name.");
        addField(
            crisprFields,
            "crisprAttempt.mutagenesisDonors[].oligoSequenceFasta",
            "Oligo sequence as FASTA format.");
        addField(
            crisprFields,
            "crisprAttempt.mutagenesisDonors[].preparationTypeName",
            "Preparation type.");

        // Reagents
        addField(crisprFields, "crisprAttempt.reagents", "Information about the reagents.");
        addField(crisprFields, "crisprAttempt.reagents[].id", "Id of the record in the system.");
        addField(crisprFields, "crisprAttempt.reagents[].reagentName", "Reagent's name.");
        addField(
            crisprFields, "crisprAttempt.reagents[].reagentDescription", "Reagent's description.");
        addField(crisprFields, "crisprAttempt.reagents[].concentration", "Reagent's concentration.");

        // Primers
        addField(
            crisprFields, "crisprAttempt.genotypePrimers", "Information about the genotype primers.");
        addField(
            crisprFields, "crisprAttempt.genotypePrimers[].id", "Id of the record in the system.");
        addField(crisprFields, "crisprAttempt.genotypePrimers[].name", "Genotype primer's name.");
        addField(
            crisprFields,
            "crisprAttempt.genotypePrimers[].genomicStartCoordinate",
            "Genomic start coordinate.");
        addField(
            crisprFields,
            "crisprAttempt.genotypePrimers[].genomicEndCoordinate",
            "Genomic end coordinate.");
        addField(
            crisprFields,
            "crisprAttempt.genotypePrimers[].genomicEndCoordinate",
            "Genotype primer name.");
        addField(
            crisprFields, "crisprAttempt.genotypePrimers[].sequence", "Genotype primer sequence.");

        // Assay
        addField(crisprFields, "crisprAttempt.assay", "Information about the assay.");
        addField(crisprFields, "crisprAttempt.assay.id", "Id of the record in the system.");
        addField(crisprFields, "crisprAttempt.assay.typeName", "Assay type.");
        addField(crisprFields, "crisprAttempt.assay.numFounderPups", "Number of founder pups.");
        addField(
            crisprFields,
            "crisprAttempt.assay.numFounderSelectedForBreeding",
            "Number of founder selected for breeding.");
        addField(crisprFields, "crisprAttempt.assay.founderNumAssays", "Number of founder assays.");
        addField(
            crisprFields,
            "crisprAttempt.assay.numDeletionG0Mutants",
            "Number of deletion G0 mutants.");
        addField(
            crisprFields,
            "crisprAttempt.assay.numG0WhereMutationDetected",
            "Number of G0 where mutations where detected.");
        addField(
            crisprFields,
            "crisprAttempt.assay.numHdrG0Mutants",
            "Number of HDR G0 mutants.");
        addField(
            crisprFields,
            "crisprAttempt.assay.numHdrG0MutantsAllDonorsInserted",
            "Number of HDR G0 mutants with all donors inserted.");
        addField(
            crisprFields,
            "crisprAttempt.assay.numHdrG0MutantsSubsetDonorsInserted",
            "Number of HDR G0 mutants with a subset of donors inserted.");
        addField(crisprFields, "crisprAttempt.assay.numHrG0Mutants", "Number of HR G0 mutants.");
        addField(crisprFields, "crisprAttempt.assay.numNhejG0Mutants", "Number of NHEJ G0 mutants.");
        addField(crisprFields, "crisprAttempt.assay.embryoTransferDay", "Embryo transfer day.");
        addField(crisprFields, "crisprAttempt.assay.totalTransferred", "Total transferred");

        return crisprFields;
    }

    public static List<FieldDescriptor> getPhenotypingFieldDescriptors()
    {
        List<FieldDescriptor> phenotypingFields = new ArrayList<>();

        addField(phenotypingFields, "phenotypingStartingPoint", "Outcome information");
        addField(phenotypingFields, "phenotypingStartingPoint._links", "");
        addField(phenotypingFields, "phenotypingStartingPoint._links.outcome", "");
        addField(phenotypingFields, "phenotypingStartingPoint._links.outcome.href", "Outcome link");
        addField(phenotypingFields, "phenotypingStartingPoint.outcomeTpo",
                "Public identifier for the outcome.");
        addField(phenotypingFields, "phenotypingAttemptResponse", "Phenotyping attempt details.");
        addField(phenotypingFields, "phenotypingAttemptResponse.phenotypingExternalRef",
                "Phenotyping colony name or specimen group name");
        addField(phenotypingFields, "phenotypingAttemptResponse.phenotypingBackgroundStrainName",
                "Phenotyping attempt background strain.");
        return phenotypingFields;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
