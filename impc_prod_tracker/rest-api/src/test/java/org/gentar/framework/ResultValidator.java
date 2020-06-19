package org.gentar.framework;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

/**
 * This class validates that a given GET link retrieves the expected json.
 */
public class ResultValidator
{
    public void validateObtainedMatchesJson(String obtainedResource, String expectedJsonPath)
    throws Exception
    {
        String expectedResource = TestResourceLoader.loadJsonFromResource(expectedJsonPath);
        validateObtainedIsExpected(obtainedResource, expectedResource);
    }

    public void validateObtainedMatchesJson(
        String obtainedResource, String expectedJsonPath, Customization[] customizations)
    throws Exception
    {
        String expectedResource = TestResourceLoader.loadJsonFromResource(expectedJsonPath);
        validateObtainedIsExpected(obtainedResource, expectedResource, customizations);
    }

    private void validateObtainedIsExpected(String obtainedJson, String expectedJson)
    {
        JSONAssert.assertEquals(obtainedJson, expectedJson, JSONCompareMode.STRICT);
    }

    private void validateObtainedIsExpected(
        String obtainedJson, String expectedJson, Customization[] customizations)
    {
        CustomComparator customComparator =
            new CustomComparator(JSONCompareMode.STRICT, customizations);
        JSONAssert.assertEquals(expectedJson, obtainedJson, customComparator);
    }
}
