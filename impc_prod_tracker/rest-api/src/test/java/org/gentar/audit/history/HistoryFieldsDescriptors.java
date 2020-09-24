package org.gentar.audit.history;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class HistoryFieldsDescriptors
{
    public static List<FieldDescriptor> getHistoryFieldDescriptions()
    {
        return getHistoryFieldDescriptions("");
    }
    public static List<FieldDescriptor> getHistoryFieldDescriptions(String prefix)
    {
        List<FieldDescriptor> historyFieldDescriptions = new ArrayList<>();
        addField(historyFieldDescriptions, prefix + "[].id", "Id to see the order.");
        addField(historyFieldDescriptions, prefix + "[].user", "User that did the change.");
        addField(historyFieldDescriptions, prefix + "[].date", "Date of the change.");
        addField(historyFieldDescriptions, prefix + "[].comment", "Comment about the change.");
        addField(historyFieldDescriptions, prefix + "[].details[]", "Details of the changes.");
        addField(historyFieldDescriptions, prefix + "[].details[].field", "Field that was changed.");
        addField(
            historyFieldDescriptions,
            prefix + "[].details[].oldValue",
            "Value before the change.",
            JsonFieldType.STRING);
        addField(
            historyFieldDescriptions,
            prefix + "[].details[].newValue",
            "Value after the change.",
            JsonFieldType.STRING);
        addField(
            historyFieldDescriptions,
            prefix + "[].details[].note",
            "One of these: Field changed, Element added, Element deleted.");
        addField(historyFieldDescriptions, "_links.self.href", "Link to the resource", JsonFieldType.STRING);
        return historyFieldDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }

    private static void addField(List<FieldDescriptor> list, String name, String description, Object type)
    {
        list.add(fieldWithPath(name).description(description).optional().type(type));
    }
}
