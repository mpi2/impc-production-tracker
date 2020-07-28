package org.gentar.biology.starting_point;

import org.gentar.Mapper;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeController;
import org.gentar.biology.outcome.OutcomeService;
import org.gentar.biology.plan.PlanController;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlanStartingPointMapper implements Mapper<PlanStartingPoint, PlanStartingPointDTO>
{
    private OutcomeService outcomeService;

    public PlanStartingPointMapper(OutcomeService outcomeService)
    {
        this.outcomeService = outcomeService;
    }

    @Override
    public PlanStartingPointDTO toDto(PlanStartingPoint planStartingPoint)
    {
        PlanStartingPointDTO planStartingPointDTO = new PlanStartingPointDTO();
        planStartingPointDTO.setId(planStartingPoint.getId());
        if (planStartingPoint.getOutcome() != null)
        {
            planStartingPointDTO.setTpo(planStartingPoint.getOutcome().getTpo());
            addOutcomeLinks(planStartingPointDTO, planStartingPoint);
            planStartingPointDTO.setPin(planStartingPoint.getOutcome().getPlan().getPin());
            addProductionPlanLinks(planStartingPointDTO, planStartingPoint);
        }
        return planStartingPointDTO;
    }

    private void addProductionPlanLinks(PlanStartingPointDTO planStartingPointDTO,
                                        PlanStartingPoint planStartingPoint)
    {
        List<Link> links = new ArrayList<>();
        List<Outcome> outcomes = new ArrayList<>();
        Outcome outcome = planStartingPoint.getOutcome();
        outcomes.add(outcome);
        if (outcomes != null)
        {
            outcomes.forEach(x ->
                    links.add(linkTo(methodOn(PlanController.class)
                            .findOne(outcome.getPlan().getPin()))
                            .withRel("productionPlan")));
        }
        planStartingPointDTO.add(links);
    }

    private void addOutcomeLinks(PlanStartingPointDTO planStartingPointDTO,
                                          PlanStartingPoint planStartingPoint)
    {
        List<Link> links = new ArrayList<>();
        List<Outcome> outcomes = new ArrayList<>();
        Outcome outcome = planStartingPoint.getOutcome();
        outcomes.add(outcome);
        if (outcomes != null)
        {
            outcomes.forEach(x ->
                    links.add(linkTo(methodOn(OutcomeController.class)
                            .findOneByPlanAndTpo(outcome.getPlan().getPin(), planStartingPointDTO.getTpo()))
                            .withRel("outcome")));
        }
        planStartingPointDTO.add(links);
    }

    @Override
    public PlanStartingPoint toEntity(PlanStartingPointDTO planStartingPointDTO)
    {
        PlanStartingPoint planStartingPoint = new PlanStartingPoint();
        planStartingPoint.setId(planStartingPointDTO.getId());
        Outcome outcome = outcomeService.getByTpoFailsIfNotFound(planStartingPointDTO.getTpo());
        planStartingPoint.setOutcome(outcome);
        return planStartingPoint;
    }
}
