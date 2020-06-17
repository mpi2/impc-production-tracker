package org.gentar.biology.outcome;

import org.gentar.biology.statemachine.StatusTransitionFieldsDescriptors;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class OutcomeFieldsDescriptors
{
    public static List<FieldDescriptor> getOutcomeFieldDescriptions()
    {
        List<FieldDescriptor> outcomeFieldDescriptions = new ArrayList<>();
        addField(outcomeFieldDescriptions, "pin", "Public identifier for the plan.");
        addField(outcomeFieldDescriptions, "tpo", "Public identifier for the outcome.");
        addField(outcomeFieldDescriptions, "outcomeTypeName", "Outcome type.");
        addField(
            outcomeFieldDescriptions, "_links.mutations.href", "Mutation associated with the outcome.");
        return outcomeFieldDescriptions;
    }

    public static List<FieldDescriptor> getColonyFieldDescriptions()
    {
        List<FieldDescriptor> outcomeFieldDescriptions = new ArrayList<>();
        addField(outcomeFieldDescriptions, "colony", "Colony information.");
        addField(
            outcomeFieldDescriptions, "colony.name", "Name of the colony. It needs to be unique.");
        addField(
            outcomeFieldDescriptions, "colony.genotypingComment", "Comment about the genotyping.");
        addField(outcomeFieldDescriptions, "colony.statusName", "Current status of the colony.");
        addField(
            outcomeFieldDescriptions, "colony.backgroundStrainName", "Name of the strain in the colony.");

        addField(
            outcomeFieldDescriptions,
            "colony.statusDates[]",
            "Stamps of the statuses the colony has gone through.");
        addField(
            outcomeFieldDescriptions,
            "colony.statusDates[].statusName",
            "Status that the colony had in a date.");
        addField(
            outcomeFieldDescriptions,
            "colony.statusDates[].date",
            "Data of the status change.");
        addField(
            outcomeFieldDescriptions,
            "colony.distributionProducts[]",
            "Distribution products associated with the colony.");
        addField(
            outcomeFieldDescriptions,
            "colony.distributionProducts[].id",
            "Id for the record in the system.");
        addField(
            outcomeFieldDescriptions,
            "colony.distributionProducts[].distributionCentreName",
            "Centre for the distribution product.");
        addField(
            outcomeFieldDescriptions,
            "colony.distributionProducts[].productTypeName",
            "Product type name.");
        addField(
            outcomeFieldDescriptions,
            "colony.distributionProducts[].distributionNetworkName",
            "Distribution Network name.");
        addField(
            outcomeFieldDescriptions,
            "colony.distributionProducts[].startDate",
            "Start date of the distribution product.");
        addField(
            outcomeFieldDescriptions,
            "colony.distributionProducts[].endDate",
            "End date of the distribution product.");
        List<FieldDescriptor> statusTransitionFieldsDescriptors =
            StatusTransitionFieldsDescriptors.getStatusTransitionFieldsDescriptors();;
        statusTransitionFieldsDescriptors.forEach(x -> {
            addField(outcomeFieldDescriptions, "colony." + x.getPath(), x.getDescription().toString());
        });
        return outcomeFieldDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
