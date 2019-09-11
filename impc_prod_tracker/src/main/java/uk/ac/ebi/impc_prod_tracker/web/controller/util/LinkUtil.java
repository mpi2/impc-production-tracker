package uk.ac.ebi.impc_prod_tracker.web.controller.util;

import org.springframework.hateoas.PagedModel;

public class LinkUtil
{
    public static String createLinkHeader(PagedModel<?> pm)
    {
        final StringBuilder linkHeader = new StringBuilder();
        if (!pm.getLinks("first").isEmpty())
        {
            linkHeader.append(buildLinkHeader(pm.getLinks("first").get(0).getHref(), "first"));
            linkHeader.append(", ");
        }
        if (!pm.getLinks("next").isEmpty())
        {
            linkHeader.append(buildLinkHeader(pm.getLinks("next").get(0).getHref(), "next"));
        }
        return linkHeader.toString();
    }

    private static String buildLinkHeader(final String uri, final String rel)
    {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }
}
