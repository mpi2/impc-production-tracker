package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;

import java.util.ArrayList;
import java.util.List;

public class ChangeResponseCustomizations
{
    public static Customization[] ignoreDates()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildDateCustomization("history[0].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("history[1].date"));
        customizations.add(buildCustomizationForSelfLink());
        return customizations.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForSelfLink()
    {
        return new Customization(
            "_links.self.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.URL_PATTERN));
    }
}
