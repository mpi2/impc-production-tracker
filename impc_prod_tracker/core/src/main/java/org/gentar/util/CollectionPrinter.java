package org.gentar.util;

import org.gentar.audit.diff.ChangeEntry;

import java.util.List;
import java.util.Map;

/**
 * Utility class to print a collections in a format that is easy to visualise.
 */
@SuppressWarnings("unchecked")
public class CollectionPrinter
{
    public static void print(Map map)
    {
        System.out.println("{");
        if (map != null)
        {
            map.forEach((k, v) ->
            {
                System.out.println("\t" + "key:[" + k + "] :: value:[" + v + "]");
            });
        }
        System.out.println("}");
    }

    public static void print(List list)
    {
        System.out.println("[");
        if (list != null)
        {
            list.forEach(x -> System.out.println("\t" + x));
        }
        System.out.println("]");
    }
}
