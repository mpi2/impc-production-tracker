package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;

import java.util.ArrayList;
import java.util.List;

public class PersonCustomizations
{
    public static  Customization[] ignoreIds()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.add(CustomizationHelper.buildIdCustomization("id"));
        customizationList.add(CustomizationHelper.buildIdCustomization("rolesWorkUnits[**].id"));
        customizationList.add(CustomizationHelper.buildIdCustomization("rolesConsortia[**].id"));
        return customizationList.toArray(new Customization[0]);
    }
}
