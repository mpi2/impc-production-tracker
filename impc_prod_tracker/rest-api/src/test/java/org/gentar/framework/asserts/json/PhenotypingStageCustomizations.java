package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates the customizations needed to compare two json objects when they contain
 * information about a phenotyping stage.
 */
public class PhenotypingStageCustomizations
{
    public static Customization[] ignoreIdsAndPsnAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForStatusDates());
        customizationList.add(buildCustomizationForPsn());
        customizationList.add(buildCustomizationForSelfLink());
        return customizationList.toArray(new Customization[0]);
    }

    public static Customization[] ignoreIdsAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForStatusDates());
        return customizationList.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForPsn()
    {
        return new Customization(
            "psn", new RegularExpressionValueMatcher<>(CustomizationConstants.PSN_PATTERN));
    }

    private static Customization buildCustomizationForSelfLink()
    {
        return new Customization(
            "_links.self.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.PSN_URL_PATTERN));
    }

    private static List<Customization> buildCustomizationForStatusDates()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildDateCustomization("statusDates[0].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("statusDates[1].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("statusDates[2].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("statusDates[3].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("summaryStatusDates[0].date"));
        return customizations;
    }
}
