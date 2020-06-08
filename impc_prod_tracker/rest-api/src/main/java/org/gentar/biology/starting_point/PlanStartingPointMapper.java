package org.gentar.biology.starting_point;

import org.gentar.Mapper;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeService;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.springframework.stereotype.Component;

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
        }
        return planStartingPointDTO;
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
