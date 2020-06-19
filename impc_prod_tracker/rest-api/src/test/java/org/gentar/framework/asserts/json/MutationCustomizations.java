package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import java.util.ArrayList;
import java.util.List;

public class MutationCustomizations
{
    public static  Customization[] ignoreIdsAndMin()
    {
        List<Customization> customizationList = new ArrayList<>();
        customizationList.addAll(buildCustomizationForMutationSequencesIds());
        customizationList.add(buildCustomizationForMin());
        return customizationList.toArray(new Customization[0]);
    }

    private static Customization buildCustomizationForMin()
    {
        return new Customization(
            "min", new RegularExpressionValueMatcher<>(CustomizationConstants.MIN_PATTERN));
    }

    private static List<Customization> buildCustomizationForMutationSequencesIds()
    {
        List<Customization> customizations = new ArrayList<>();
        customizations.add(
            CustomizationHelper.buildIdCustomization("mutationSequences[0].id"));
        return customizations;
    }

}
