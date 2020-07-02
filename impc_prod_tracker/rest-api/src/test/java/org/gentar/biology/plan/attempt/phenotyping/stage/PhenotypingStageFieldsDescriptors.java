package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class PhenotypingStageFieldsDescriptors
{
    public static List<FieldDescriptor> getPhenotypingStageFieldsDescriptions()
    {
        List<FieldDescriptor> phenotypingStageFieldsDescriptions = new ArrayList<>();

        addField(phenotypingStageFieldsDescriptions, "pin", "Public identifier for the phenotyping plan.");
        addField(phenotypingStageFieldsDescriptions, "psn", "Public identifier for the phenotyping stage.");
        addField(phenotypingStageFieldsDescriptions, "statusName", "");
        addField(phenotypingStageFieldsDescriptions, "phenotypingTypeName", "");
        addField(phenotypingStageFieldsDescriptions, "phenotypingExternalRef", "");
        addField(phenotypingStageFieldsDescriptions, "phenotypingExperimentsStarted", "");
        addField(phenotypingStageFieldsDescriptions, "doNotCountTowardsCompleteness", "");
        addField(phenotypingStageFieldsDescriptions, "initialDataReleaseDate", "");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[]", "");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].id", "");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].startDate", "");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].endDate", "");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].workUnitName", "");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].materialDepositedTypeName", "");
        addField(phenotypingStageFieldsDescriptions, "statusDates[]", "");
        addField(phenotypingStageFieldsDescriptions, "statusDates[].statusName", "");
        addField(phenotypingStageFieldsDescriptions, "statusDates[].date", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.currentStatus", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[]", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].action", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].description", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].triggeredByUser", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].available", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].note", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].nextStatus", "");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.actionToExecute", "");

        return phenotypingStageFieldsDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
