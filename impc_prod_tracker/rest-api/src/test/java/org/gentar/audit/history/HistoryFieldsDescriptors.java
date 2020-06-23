package org.gentar.audit.history;

import org.springframework.restdocs.payload.FieldDescriptor;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class HistoryFieldsDescriptors
{
    public static List<FieldDescriptor> getHistoryFieldDescriptions()
    {
        List<FieldDescriptor> historyFieldDescriptions = new ArrayList<>();
        addField(historyFieldDescriptions, "[].id", "Id to see the order.");
        addField(historyFieldDescriptions, "[].user", "User that did the change.");
        addField(historyFieldDescriptions, "[].date", "Date of the change.");
        addField(historyFieldDescriptions, "[].comment", "Comment about the change.");
        addField(historyFieldDescriptions, "[].details[]", "Details of the changes.");
        addField(historyFieldDescriptions, "[].details[].field", "Field that was changed.");
        addField(historyFieldDescriptions, "[].details[].oldValue", "Value before the change.");
        addField(historyFieldDescriptions, "[].details[].newValue", "Value after the change.");
        return historyFieldDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
