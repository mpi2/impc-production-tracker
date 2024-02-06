package org.gentar.helpers;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanController;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.Project;
import org.springframework.hateoas.Link;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
                    .toList();
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
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
