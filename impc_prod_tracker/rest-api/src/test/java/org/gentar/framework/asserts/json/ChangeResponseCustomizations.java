package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;

import java.util.ArrayList;
import java.util.List;

public class ChangeResponseCustomizations
{
    public static Customization[] ignoreDates()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildDateCustomization("history[0].date"));
        return customizations.toArray(new Customization[0]);
    }
}
