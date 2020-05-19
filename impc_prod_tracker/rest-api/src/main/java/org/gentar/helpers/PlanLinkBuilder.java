package org.gentar.helpers;

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
                x -> links.add(
                    linkTo(PlanController.class).slash(x.getPin()).withRel(groupOfLinksName)));
        }

        return links;
    }
}
