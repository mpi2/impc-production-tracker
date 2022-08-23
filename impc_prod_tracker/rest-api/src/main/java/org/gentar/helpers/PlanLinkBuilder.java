package org.gentar.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.gentar.biology.plan.PlanController;
import org.gentar.biology.plan.type.PlanTypeName;
import org.springframework.hateoas.Link;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class that creates links for plans.
 */
public class PlanLinkBuilder
{
    public static List<Link> buildPlanLinks(Project project, PlanTypeName planType, String groupOfLinksName)
    {

        List<Link> links = new ArrayList<>();
        Set<Plan> plans = project.getPlans();
        if (plans != null)
        {
            List<Plan> plansByType =
                plans.stream().filter(
                    x -> planType.getLabel().equalsIgnoreCase(x.getPlanType().getName()))
                    .collect(Collectors.toList());
            plansByType.forEach(
                x -> {
                    Link link =
                        linkTo(PlanController.class).slash(x.getPin()).withRel(groupOfLinksName);
                    link = link.withHref(decode(link.getHref()));
                    links.add(link);
                });
        }

        return links;
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
