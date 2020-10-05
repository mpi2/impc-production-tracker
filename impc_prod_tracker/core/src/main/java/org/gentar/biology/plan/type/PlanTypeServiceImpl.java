package org.gentar.biology.plan.type;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class PlanTypeServiceImpl implements PlanTypeService
{
    private final PlanTypeRepository planTypeRepository;
    private static final String PLAN_TYPE_NOT_FOUND_ERROR = "Plan type '%s' does not exist.";

    public PlanTypeServiceImpl(PlanTypeRepository planTypeRepository)
    {
        this.planTypeRepository = planTypeRepository;
    }

    @Override
    public PlanType getPlanTypeByName(String name)
    {
        return planTypeRepository.findFirstByNameIgnoreCase(name);
    }

    @Override
    public PlanType getPlanTypeByNameFailsIfNull(String name)
    {
        PlanType planType = getPlanTypeByName(name);
        if (planType == null)
        {
            throw new UserOperationFailedException(String.format(PLAN_TYPE_NOT_FOUND_ERROR, name));
        }
        return planType;
    }
}
