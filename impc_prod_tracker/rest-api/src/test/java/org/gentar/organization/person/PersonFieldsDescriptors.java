package org.gentar.organization.person;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class PersonFieldsDescriptors
{
    public static List<FieldDescriptor> getPersonFieldDescriptions()
    {
        List<FieldDescriptor> personFieldDescriptions = new ArrayList<>();
        addField(personFieldDescriptions, "name", "User name in the system.");
        addField(personFieldDescriptions, "email", "User email");
        addField(
            personFieldDescriptions,
            "contactable",
            "Whether or not the user can be contacted through their email.");
        addField(
            personFieldDescriptions,
            "rolesWorkUnits[]",
            "Information about the work units that the user is associated with, as long as the role the user has in each work unit.");
        addField(
            personFieldDescriptions,
            "rolesWorkUnits[].id",
            "Internal id of the record in the system.");
        addField(personFieldDescriptions, "rolesWorkUnits[].workUnitName", "Name of the work unit.");
        addField(personFieldDescriptions, "rolesWorkUnits[].roleName", "Name of the role.");
        addField(
            personFieldDescriptions,
            "rolesConsortia[]",
            "Information about the consortia that the user is associated with, as long as the role the user has in each consortium.");
        addField(
            personFieldDescriptions, "rolesConsortia[].id", "Internal id of the record in the system.");
        addField(
            personFieldDescriptions, "rolesConsortia[].consortiumName", "Name of the consortium.");
        addField(
            personFieldDescriptions, "rolesConsortia[].roleName", "Name of the role.");
        addField(
            personFieldDescriptions, "isAdmin", "Whether or not the user is admin.");
        addField(
            personFieldDescriptions,
            "actionPermissions[]",
            "Information about the general permissions the user has.");
        addField(personFieldDescriptions, "actionPermissions[].actionName", "Action name.");
        addField(
            personFieldDescriptions,
            "actionPermissions[].value",
            "True if the user has permission. False otherwise.");
        return personFieldDescriptions;
    }

    private static void addField(List<FieldDescriptor> list, String name, String description)
    {
        list.add(fieldWithPath(name).description(description).optional());
    }
}
