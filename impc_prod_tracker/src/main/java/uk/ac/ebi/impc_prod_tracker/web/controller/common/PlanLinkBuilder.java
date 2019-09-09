package uk.ac.ebi.impc_prod_tracker.web.controller.common;

import org.springframework.hateoas.Link;
import uk.ac.ebi.impc_prod_tracker.common.types.PlanTypes;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.web.controller.plan.PlanController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Class that creates links for plans.
 */
public class PlanLinkBuilder
{
    public static List<Link> buildPlanLinks(Project project, PlanTypes planType, String groupOfLinksName)
    {

        List<Link> links = new ArrayList<>();
        List<Plan> plans =
            project.getPlans().stream().filter(
                x -> planType.getTypeName().equalsIgnoreCase(x.getPlanType().getName()))
                .collect(Collectors.toList());
        plans.forEach(
            x -> links.add(
                linkTo(PlanController.class).slash(x.getPin()).withRel(groupOfLinksName)));
        return links;
    }
}
