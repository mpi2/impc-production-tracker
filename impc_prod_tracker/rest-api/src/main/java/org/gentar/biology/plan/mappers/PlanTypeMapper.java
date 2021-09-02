package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeService;
import org.springframework.stereotype.Component;

@Component
public class PlanTypeMapper implements Mapper<PlanType, String>
{
    private final PlanTypeService planTypeService;

    public PlanTypeMapper(PlanTypeService planTypeService)
    {
        this.planTypeService = planTypeService;
    }

    @Override
    public String toDto(PlanType entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public PlanType toEntity(String name)
    {
        return planTypeService.getPlanTypeByNameFailsIfNull(name);
    }
}
