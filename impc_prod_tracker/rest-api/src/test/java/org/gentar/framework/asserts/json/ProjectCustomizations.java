package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;

import java.util.ArrayList;
import java.util.List;

public class ProjectCustomizations
{
    public Customization[] ignoreIdsAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.add(buildCustomizationForTpn());
        customizationList.add(buildCustomizationForPin());
        customizationList.addAll(buildCustomizationForStatusDates());
        customizationList.add(buildCustomizationForSelfLink());
        customizationList.addAll(buildCustomizationForPlansLinks());
        return customizationList.toArray(new Customization[0]);
    }

    private Customization buildCustomizationForTpn()
    {
        return new Customization(
            "tpn", new RegularExpressionValueMatcher<>(CustomizationConstants.TPN_PATTERN));
    }

    private Customization buildCustomizationForPin()
    {
        return new Customization(
            "pin", new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_PATTERN));
    }

    private List<Customization> buildCustomizationForStatusDates()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildDateCustomization("assignmentStatusStamps[0].date"));
        return customizations;
    }

    private static Customization buildCustomizationForSelfLink()
    {
        return new Customization(
            "_links.self.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.TPN_URL_PATTERN));
    }

    private List<Customization> buildCustomizationForPlansLinks()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(new Customization(
            "_links.productionPlans.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_URL_PATTERN)));
        customizations.add(new Customization(
            "_links.phenotypingPlans.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_URL_PATTERN)));
        return customizations;
    }
}
