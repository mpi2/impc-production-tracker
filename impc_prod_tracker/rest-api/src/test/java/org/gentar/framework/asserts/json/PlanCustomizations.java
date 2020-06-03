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
    public Customization[] ignoreIdsAndPinAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForCrisprIds());
        customizationList.addAll(buildCustomizationForStatusDates());
        customizationList.add(buildCustomizationForPin());
        return customizationList.toArray(new Customization[0]);
    }

    public Customization[] ignoreIdsAndDates()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForCrisprIds());
        customizationList.addAll(buildCustomizationForStatusDates());
        return customizationList.toArray(new Customization[0]);
    }

    private Customization buildCustomizationForPin()
    {
        return new Customization("pin", new RegularExpressionValueMatcher<>("PIN:\\d{1,10}"));
    }

    private List<Customization> buildCustomizationForCrisprIds()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(CustomizationHelper.buildIdCustomization("crisprAttempt.assay.id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.genotypePrimers[0].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.guides[0].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.mutagenesisDonors[0].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.nucleases[0].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.reagents[0].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.mutagenesisDonors[0].id"));
        customizations.add(
            CustomizationHelper.buildIdCustomization("crisprAttempt.mutagenesisDonors[0].id"));
        return customizations;
    }

    private List<Customization> buildCustomizationForStatusDates()
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
