package org.gentar.framework;

import org.gentar.util.JsonHelper;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class allows to to load a resource required for a test.
 */
public class TestResourceLoader
{
    public static <T> T loadTestResource(String resourcePath, Class<T> resourceClass)
    throws IOException
    {
        InputStream is = TestResourceLoader.class.getResourceAsStream(resourcePath);
        return JsonHelper.fromJson(is, resourceClass);
    }
}
