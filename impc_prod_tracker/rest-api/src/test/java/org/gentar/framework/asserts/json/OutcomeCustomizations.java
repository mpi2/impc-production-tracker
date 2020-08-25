package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;

import java.util.ArrayList;
import java.util.List;

public class OutcomeCustomizations
{
    public static Customization[] ignoreIdsAndTpoAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForColonyIds());
        customizationList.addAll(buildCustomizationForColonyStatusDates());
        customizationList.add(buildCustomizationForTpo());
        customizationList.add(buildCustomizationForTpoLink());
        customizationList.add(buildCustomizationForMinLink());
        return customizationList.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForTpoLink()
    {
        return new Customization(
            "_links.self.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.TPO_URL_PATTERN));
    }

    private static Customization buildCustomizationForMinLink()
    {
        return new Customization(
            " _links.mutations[0].href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.MIN_URL_PATTERN));
    }

    public static Customization[] ignoreIdsAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForColonyIds());
        customizationList.addAll(buildCustomizationForColonyStatusDates());
        return customizationList.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForTpo()
    {
        return new Customization(
            "top", new RegularExpressionValueMatcher<>(CustomizationConstants.TPO_PATTERN));
    }

    private static List<Customization> buildCustomizationForColonyIds()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(
            CustomizationHelper.buildIdCustomization("colony.distributionProducts[0].id"));
        return customizations;
    }

    private static List<Customization> buildCustomizationForColonyStatusDates()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildDateCustomization("colony.statusDates[0].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("colony.statusDates[1].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("colony.statusDates[2].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("colony.statusDates[3].date"));
        customizations.add(
            CustomizationHelper.buildDateCustomization("colony.summaryStatusDates[0].date"));
        return customizations;
    }
}
