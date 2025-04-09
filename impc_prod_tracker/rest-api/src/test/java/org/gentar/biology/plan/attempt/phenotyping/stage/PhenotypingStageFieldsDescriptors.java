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
        addField(phenotypingStageFieldsDescriptions, "statusName", "Current status of the phenotyping stage.");
        addField(phenotypingStageFieldsDescriptions, "phenotypingTypeName", "Phenotyping attempt type.");
        addField(phenotypingStageFieldsDescriptions, "phenotypingExternalRef", "Colony name or Specimen Group name.");
        addField(phenotypingStageFieldsDescriptions, "phenotypingExperimentsStarted",
                "Date when the phenotyping experiments started.");
        addField(
            phenotypingStageFieldsDescriptions,
            "initialDataReleaseDate", "Date when some data for the line becomes public. This is set by the CDA");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[]",
                "Tissue distribution available. ");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].id",
                "Id of the record in the system.");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].startDate",
                "Tissue distribution starting date.");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].endDate",
                "Tissue distribution ending date.");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].workUnitName",
                "Work unit responsible for the distribution.");
        addField(phenotypingStageFieldsDescriptions, "tissueDistributions[].materialDepositedTypeName",
                "Type of tissue available.");
        addField(phenotypingStageFieldsDescriptions, "statusDates[]",
                "Stamps of the statuses the phenotyping stage has gone through.");
        addField(phenotypingStageFieldsDescriptions, "statusDates[].statusName",
                "Status name.");
        addField(phenotypingStageFieldsDescriptions, "statusDates[].date",
                "Date when the status record was saved in the system");
        addField(phenotypingStageFieldsDescriptions, "statusTransition",
                "Information about the current state in the state machine for the phenotyping stage.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.currentStatus",
                "Current status in the phenotyping stage.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[]",
                "StatusTransitions in the state machine given the current state.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].action",
                "Action or statusTransition's name.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].description",
                "StatusTransition's description.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].triggeredByUser",
                "Indicates whether the statusTransition is executed by the user or by the system.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].available",
                "Indicates if the StatusTransition can be executed at the moment.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].note",
                "Additional explanation about the availability to execute the statusTransition.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.transitions[].nextStatus",
                "Next status that the plan will have if the statusTransition is executed.");
        addField(phenotypingStageFieldsDescriptions, "statusTransition.actionToExecute",
                "Name of the statusTransition (action) to execute.");
        addField(phenotypingStageFieldsDescriptions, "_links.self.href", "Link to the phenotyping stage.");
        addField(phenotypingStageFieldsDescriptions, "_links.plan.href", "Link to the plan.");
        return phenotypingStageFieldsDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
