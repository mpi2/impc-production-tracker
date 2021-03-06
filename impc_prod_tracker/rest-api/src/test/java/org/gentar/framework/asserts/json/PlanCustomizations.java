package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates the customizations needed to compare two json objects when they contain
 * information about a plan.
 */
public class PlanCustomizations
{
    public static Customization[] ignoreIdsAndPinAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForCrisprIds());
        customizationList.addAll(buildCustomizationForStatusDates());
        customizationList.add(buildCustomizationForPin());
        customizationList.add(buildCustomizationForPinLink());
        return customizationList.toArray(new Customization[0]);
    }

    public static Customization[] ignoreIdsAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForCrisprIds());
        customizationList.addAll(buildCustomizationForStatusDates());
        return customizationList.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForPin()
    {
        return new Customization(
            "pin", new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_PATTERN));
    }

    private static Customization buildCustomizationForPinLink()
    {
        return new Customization(
            "_links.self.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.PIN_URL_PATTERN));
    }

    private static List<Customization> buildCustomizationForCrisprIds()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildIdCustomization("crisprAttempt.assay.id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.genotypePrimers[**].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.guides[**].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.mutagenesisDonors[**].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.nucleases[**].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.reagents[**].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.mutagenesisDonors[**].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.mutagenesisDonors[**].id"));
        return customizations;
    }

    private static List<Customization> buildCustomizationForStatusDates()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildDateCustomization("statusDates[**].date"));
        customizations.add(CustomizationHelper.buildDateCustomization("summaryStatusDates[**].date"));
        return customizations;
    }

}
