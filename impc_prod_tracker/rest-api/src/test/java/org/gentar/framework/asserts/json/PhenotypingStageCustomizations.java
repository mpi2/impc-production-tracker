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
        customizationList.add(buildCustomizationForStatusDates());
        customizationList.add(buildCustomizationForPsn());
        customizationList.add(buildCustomizationForSelfLink());
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

    private static Customization buildCustomizationForStatusDates()
    {
        return CustomizationHelper.buildDateCustomization("statusDates[**].date");
    }
}
