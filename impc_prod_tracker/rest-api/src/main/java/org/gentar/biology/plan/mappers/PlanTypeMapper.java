package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class PlanTypeMapper implements Mapper<PlanType, String>
{
    private PlanTypeService planTypeService;

    private static final String PLAN_TYPE_NOT_FOUND_ERROR = "Plan type '%s' does not exist.";

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
            throw new UserOperationFailedException(String.format(PLAN_TYPE_NOT_FOUND_ERROR, planType));
        }

        return planType;
    }
}
