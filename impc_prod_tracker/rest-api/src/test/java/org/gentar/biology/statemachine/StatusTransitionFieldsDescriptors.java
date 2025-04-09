package org.gentar.biology.statemachine;

import org.springframework.restdocs.payload.FieldDescriptor;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class StatusTransitionFieldsDescriptors
{
    public static List<FieldDescriptor> getStatusTransitionFieldsDescriptors()
    {
        List<FieldDescriptor> sharedFieldDescriptions = new ArrayList<>();
        addField(
            sharedFieldDescriptions,
            "statusTransition",
            "Information about the current state in the state machine for the plan.");
        addField(sharedFieldDescriptions, "statusTransition.currentStatus", "Current status in the plan");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[]",
            "StatusTransitions in the state machine given the current state.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].action",
            "Action or statusTransition's name.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].description",
            "StatusTransition's description.");
        addField(sharedFieldDescriptions,
            "statusTransition.transitions[].triggeredByUser",
            "Indicates whether the statusTransition is executed by the user or by the system.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].available",
            "Indicates if the StatusTransition can be executed at the moment.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].note",
            "Additional explanation about the availability to execute the statusTransition.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.transitions[].nextStatus",
            "Next status that the plan will have if the statusTransition is executed.");
        addField(
            sharedFieldDescriptions,
            "statusTransition.actionToExecute",
            "Name of the statusTransition (action) to execute.");
        return sharedFieldDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
