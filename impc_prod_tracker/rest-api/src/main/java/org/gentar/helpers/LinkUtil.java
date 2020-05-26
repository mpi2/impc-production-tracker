package org.gentar.helpers;

import org.json.JSONObject;
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

    /**
     * Gets the self.href link from a json. It expects the json to have that structure.
     * @param jsonString The json string where the link is.
     * @return The link in a string representation.
     */
    public static String getSelfHrefLinkStringFromJson(String jsonString)
    {
        JSONObject jsonResponse = new JSONObject(jsonString);
        JSONObject links = jsonResponse.getJSONObject("_links");
        JSONObject self = links.getJSONObject("self");
        return self.get("href").toString();
    }
}
