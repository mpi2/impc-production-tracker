package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;

import java.util.ArrayList;
import java.util.List;

public class ProjectCustomizations
{
    public static Customization[] ignoreIdsAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.add(buildCustomizationForTpn());
        customizationList.add(buildCustomizationForPin());
        customizationList.add(buildCustomizationForConsortiaInstitutesId());
        customizationList.addAll(buildCustomizationForStatusDates());
        customizationList.add(buildCustomizationForSelfLink());
        customizationList.addAll(buildCustomizationForPlansLinks());
        return customizationList.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForConsortiaInstitutesId()
    {
        return CustomizationHelper.buildIdCustomization("consortia[0].id");
    }

    private static Customization buildCustomizationForTpn()
    {
        return new Customization(
            "tpn", new RegularExpressionValueMatcher<>(CustomizationConstants.TPN_PATTERN));
    }

    private static Customization buildCustomizationForPin()
    {
        return new Customization(
            "pin", new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_PATTERN));
    }

    private static List<Customization> buildCustomizationForStatusDates()
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

    private static List<Customization> buildCustomizationForPlansLinks()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(new Customization(
            "_links.productionPlans[0].href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_URL_PATTERN)));
        customizations.add(new Customization(
            "_links.phenotypingPlans[0].href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_URL_PATTERN)));
        return customizations;
    }
}
