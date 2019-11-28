package org.gentar.biology.plan;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class PlanTypeMapper implements Mapper<PlanType, String>
{
    private PlanTypeService planTypeService;

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
        PlanType planType = planTypeService.getPlanTypeByName(name);
        if (planType == null)
        {
            planType = new PlanType();
            planType.setName(name);
        }
        return planType;
    }
}
