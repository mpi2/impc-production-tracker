package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import java.util.ArrayList;
import java.util.List;

public class MutationCustomizations
{
    public static Customization[] ignoreIdsAndMin()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForMutationSequencesIds());
        customizationList.add(buildCustomizationForMinLink());
        customizationList.add(buildCustomizationForMin());
        return customizationList.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForMin()
    {
        return new Customization(
            "min", new RegularExpressionValueMatcher<>(CustomizationConstants.MIN_PATTERN));
    }

    private static Customization buildCustomizationForMinLink()
    {
        return new Customization(
            "_links.self.href",
            new RegularExpressionValueMatcher<>(CustomizationConstants.MIN_URL_PATTERN));
    }

    private static List<Customization> buildCustomizationForMutationSequencesIds()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(
            CustomizationHelper.buildIdCustomization("mutationSequences[**].id"));
        return customizations;
    }

}
