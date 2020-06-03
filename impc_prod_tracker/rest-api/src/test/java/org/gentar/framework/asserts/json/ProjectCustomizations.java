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
        customizations.add(CustomizationHelper.buildDateCustomization("assignmentStatusStamps[0].date"));
        return customizations;
    }

    private List<Customization> buildCustomizationForPlansLinks()
    {
        List<Customization> customizations = new ArrayList<>();
        Customization productionPlansLinks = new Customization(
            "_links.productionPlans.href",
            new RegularExpressionValueMatcher<>("(http://localhost:8080/api/plans/)(PIN:\\d{1,10})"));
        customizations.add(productionPlansLinks);
        return customizations;
    }
}
