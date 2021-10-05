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
        addField(sharedFieldDescriptions, "statusDates[]",
                "Stamps of the statuses the plan has gone through.");
        addField(sharedFieldDescriptions, "statusDates[].statusName",
                "Status that the plan had on a specific date.");
        addField(sharedFieldDescriptions, "statusDates[].date",
                "Date of the status change.");
        addField(
            sharedFieldDescriptions,
            "summaryStatusName",
            "A status summarising the global status based on the statuses of the children of the plan.");
        addField(sharedFieldDescriptions, "_links.self.href", "Link to the plan.");
        addField(sharedFieldDescriptions, "_links.project.href", "Link to the project.");
        addField(
            sharedFieldDescriptions,
            "summaryStatusDates[]",
            "Stamps of the statuses summaries the plan has gone through.");
        addField(
                sharedFieldDescriptions,
                "summaryStatusDates[].statusName",
                "Summary status that the plan had on a specific date.");
        addField(
                sharedFieldDescriptions,
                "summaryStatusDates[].date",
                "Date of the summary status change.");
        addField(sharedFieldDescriptions, "funderNames", "Funders for the plan.");
        addField(sharedFieldDescriptions, "workUnitName", "Work unit for the plan.");
        addField(sharedFieldDescriptions, "workGroupName", "Work group for the plan.");
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
        addField(crisprFields, "crisprAttempt.embryoTransferDay", "Embryo transfer day.");
        addField(crisprFields, "crisprAttempt.totalTransferred", "Total transferred");

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
        addField(crisprFields, "crisprAttempt.guides[].pam", "PAM");
        addField(crisprFields, "crisprAttempt.guides[].guideSequence", "Guide sequence");
        addField(crisprFields, "crisprAttempt.guides[].reversed",
                "Whether or not the sequence was reversed");
        addField(crisprFields, "crisprAttempt.guides[].sangerService",
                "Whether or not the sequence come from the sanger wge service");
        addField(crisprFields, "crisprAttempt.guides[].formatName", "Format of the guide. For example: sgRNA.");
        addField(crisprFields, "crisprAttempt.guides[].sourceName", "Source of the guide. For example: in-house.");

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

        return crisprFields;
    }

    public static List<FieldDescriptor> getPhenotypingFieldDescriptors()
    {
        List<FieldDescriptor> phenotypingFields = new ArrayList<>();

        addField(phenotypingFields, "phenotypingStartingPoint", "Outcome information");
        addField(phenotypingFields, "phenotypingStartingPoint._links", "");
        addField(phenotypingFields, "phenotypingStartingPoint._links.outcome", "");
        addField(phenotypingFields, "phenotypingStartingPoint._links.outcome.href", "Outcome link");
        addField(phenotypingFields, "phenotypingStartingPoint._links.productionPlan", "");
        addField(phenotypingFields, "phenotypingStartingPoint._links.productionPlan.href",
                "Production plan link");
        addField(phenotypingFields, "phenotypingStartingPoint.outcomeTpo",
                "Public identifier for the outcome.");
        addField(phenotypingFields, "phenotypingStartingPoint.productionPlanPin",
                "Public identifier for the outcome production plan.");
        addField(phenotypingFields, "phenotypingAttemptResponse", "Phenotyping attempt details.");
        addField(phenotypingFields, "phenotypingAttemptResponse.phenotypingExternalRef",
                "Phenotyping colony name or specimen group name");
        addField(phenotypingFields, "phenotypingAttemptResponse.phenotypingBackgroundStrainName",
                "Phenotyping attempt background strain.");
        addField(phenotypingFields, "phenotypingAttemptResponse.doNotCountTowardsCompleteness", "");
        addField(phenotypingFields, "phenotypingAttemptResponse.cohortProductionWorkUnitName", "");
        return phenotypingFields;
    }

    public static List<FieldDescriptor> getEsCellAlleleModificationFieldDescriptors()
    {
        List<FieldDescriptor> esCellAlleleModificationFields = new ArrayList<>();

        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint", "Outcome information");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint._links", "");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint._links.outcome", "");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint._links.outcome.href", "Outcome link");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint._links.productionPlan", "");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint._links.productionPlan.href",
                "Production plan link");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint.outcomeTpo",
                "Public identifier for the outcome.");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationStartingPoint.productionPlanPin",
                "Public identifier for the outcome production plan.");

        addField(esCellAlleleModificationFields, "esCellAlleleModificationAttempt", "ES cell allele modification attempt details.");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationAttempt.modificationExternalRef",
                "External reference used to track the modification. For data exported from iMits the mouse allele modification colony name has been recorded as the modification external reference.");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationAttempt.numberOfCreMatingsSuccessful",
                "records the number of successful Cre matings.");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationAttempt.tatCre", "Indicates tat Cre used for modification rather than a deleter strain.");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationAttempt.deleterStrainName", "ES cell allele modification attempt deleter strain");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationAttempt.imitsMouseAlleleModId", "iMits Moude Allele Modification identifier, for legacy data");
        addField(esCellAlleleModificationFields, "esCellAlleleModificationAttempt.imitsTargRepAlleleId", "TargRep Allele identifier");
        return esCellAlleleModificationFields;
    }

    public static List<FieldDescriptor> getEsCellFieldDescriptors()
    {
        List<FieldDescriptor> esCellFields = new ArrayList<>();

        addField(esCellFields, "esCellAttempt", "ES cell attempt details.");
        addField(esCellFields, "esCellAttempt.esCellName","The ES Cell used in the attempt.");
        addField(esCellFields, "esCellAttempt.imitsMiAttemptId", "The iMits attempt identifier (This is included if the data was migrated from iMits).");
        addField(esCellFields, "esCellAttempt.miDate", "The date when the micro-injection was performed.");
        addField(esCellFields, "esCellAttempt.attemptExternalRef", "External reference used to track the ES cell attempt.");
        addField(esCellFields, "esCellAttempt.experimental", "A flag indicating if the attempt is experimental.");
        addField(esCellFields, "esCellAttempt.comment", "A comment about the ES cell attempt.");
        addField(esCellFields, "esCellAttempt.blastStrainName", "Name of the blastocyst strain.");
        addField(esCellFields, "esCellAttempt.totalBlastsInjected", "Number of blastocysts injected.");
        addField(esCellFields, "esCellAttempt.totalTransferred", "Number of blastocysts transferred.");
        addField(esCellFields, "esCellAttempt.numberSurrogatesReceiving", "Number of surrogate mothers recieving blastocysts.");
        addField(esCellFields, "esCellAttempt.totalPupsBorn", "Number of pups born.");
        addField(esCellFields, "esCellAttempt.totalFemaleChimeras", "Total number of female chimeras.");
        addField(esCellFields, "esCellAttempt.totalMaleChimeras", "Total number of male chimeras.");
        addField(esCellFields, "esCellAttempt.totalChimeras", "Total number of chimeras.");
        addField(esCellFields, "esCellAttempt.numberOfMalesWith0To39PercentChimerism", "Number of male offspring with 0-39 % chimerism.");
        addField(esCellFields, "esCellAttempt.numberOfMalesWith40To79PercentChimerism", "Number of male offspring with 40-79 % chimerism.");
        addField(esCellFields, "esCellAttempt.numberOfMalesWith80To99PercentChimerism", "Number of male offspring with 80-99 % chimerism.");
        addField(esCellFields, "esCellAttempt.numberOfMalesWith100PercentChimerism", "Number of male offspring with 100 % chimerism.");
        addField(esCellFields, "esCellAttempt.testCrossStrainName", "Name of the strain used for the test cross.");
        addField(esCellFields, "esCellAttempt.dateChimerasMated", "The date when the chimeras were mated.");
        addField(esCellFields, "esCellAttempt.numberOfChimeraMatingsAttempted", "Number of matings set up with chimeras.");
        addField(esCellFields, "esCellAttempt.numberOfChimeraMatingsSuccessful", "Number of successful chimera matings.");
        addField(esCellFields, "esCellAttempt.numberOfChimerasWithGltFromCct", "Number of chimeras with germ line transmission assessed by coat-colour type.");
        addField(esCellFields, "esCellAttempt.numberOfChimerasWithGltFromGenotyping", "Number of chimeras with germ line transmission assessed by genotyping.");
        addField(esCellFields, "esCellAttempt.numberOfChimerasWith0To9PercentGlt", "Number of chimeras with 0-9 % germ line transmission.");
        addField(esCellFields, "esCellAttempt.numberOfChimerasWith10To49PercentGlt", "Number of chimeras with 10-49 % germ line transmission.");
        addField(esCellFields, "esCellAttempt.numberOfChimerasWith50To99PercentGlt", "Number of chimeras with 50-99 % germ line transmission.");
        addField(esCellFields, "esCellAttempt.numberOfChimerasWith100PercentGlt", "Number of chimeras with 100 % germ line transmission.");
        addField(esCellFields, "esCellAttempt.totalF1MiceFromMatings", "Total number of F1 offspring from matings.");
        addField(esCellFields, "esCellAttempt.numberOfCctOffspring", "");
        addField(esCellFields, "esCellAttempt.cassetteTransmissionVerified", "The date when cassette transmission was confirmed.");
        addField(esCellFields, "esCellAttempt.numberOfHetOffspring", "Number of heterozygous offspring.");
        addField(esCellFields, "esCellAttempt.numberOfLiveGltOffspring", "Number of live germ line transmission offspring.");
        return esCellFields;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
