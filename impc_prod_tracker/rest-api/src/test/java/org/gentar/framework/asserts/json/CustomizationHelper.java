package org.gentar.framework.asserts.json;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;

public class CustomizationHelper
{
    private static final String DIGIT_PATTERN = "-?[1-9]\\d*|0\"";

    /**
     * Created a customization where the comparison between two objects is true if the value to
     * compare is a digit.
     * @param path Json Path.
     * @return Customization object.
     */
    public static Customization buildIdCustomization(String path)
    {
        return new Customization(path, new RegularExpressionValueMatcher<>(DIGIT_PATTERN));
    }

    /**
     * Created a customization where the comparison between two objects is true if the value to
     * compare is a date.
     * @param path Json Path.
     * @return Customization object.
     */
    public static Customization buildDateCustomization(String path)
    {
        return new Customization(path, new DateValueMatcher<>());
    }
}
