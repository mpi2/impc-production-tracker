package org.gentar.biology.starting_point;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.plan_starting_point.PlanStartingPointDTO;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.springframework.stereotype.Component;

@Component
public class PlanStartingPointMapper implements Mapper<PlanStartingPoint, PlanStartingPointDTO>
{
    private EntityMapper entityMapper;

    public PlanStartingPointMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public PlanStartingPointDTO toDto(PlanStartingPoint planStartingPoint)
    {
        return entityMapper.toTarget(planStartingPoint, PlanStartingPointDTO.class);
    }

    @Override
    public PlanStartingPoint toEntity(PlanStartingPointDTO planStartingPointDTO)
    {
        return entityMapper.toTarget(planStartingPointDTO, PlanStartingPoint.class);
    }
}
