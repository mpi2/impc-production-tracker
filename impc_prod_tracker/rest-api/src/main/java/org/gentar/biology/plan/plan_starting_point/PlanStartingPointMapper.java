package org.gentar.biology.plan.plan_starting_point;

import org.gentar.Mapper;
import org.gentar.biology.outcome.OutcomeMapper;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.springframework.stereotype.Component;

@Component
public class PlanStartingPointMapper implements Mapper<PlanStartingPoint, PlanStartingPointDTO>
{
    private OutcomeMapper outcomeMapper;

    public PlanStartingPointMapper (OutcomeMapper outcomeMapper)
    {
        this.outcomeMapper = outcomeMapper;
    }

    @Override
    public PlanStartingPointDTO toDto(PlanStartingPoint entity) {
        return null;
    }

    @Override
    public PlanStartingPoint toEntity(PlanStartingPointDTO dto) {
        // TODO starting point
        return null;
    }


}
