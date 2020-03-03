package org.gentar.biology.plan.type;

import org.springframework.stereotype.Component;

@Component
public class PlanTypeServiceImpl implements PlanTypeService
{
    private PlanTypeRepository planTypeRepository;

    public PlanTypeServiceImpl(PlanTypeRepository planTypeRepository)
    {
        this.planTypeRepository = planTypeRepository;
    }

    public PlanType getPlanTypeByName(String name)
    {
        return planTypeRepository.findFirstByNameIgnoreCase(name);
    }
}
