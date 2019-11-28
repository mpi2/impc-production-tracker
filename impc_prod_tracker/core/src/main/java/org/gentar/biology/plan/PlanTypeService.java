package org.gentar.biology.plan;

import org.springframework.stereotype.Component;

@Component
public class PlanTypeService
{
    private PlanTypeRepository planTypeRepository;

    public PlanTypeService(PlanTypeRepository planTypeRepository)
    {
        this.planTypeRepository = planTypeRepository;
    }

    public PlanType getPlanTypeByName(String name)
    {
        return planTypeRepository.findFirstByNameIgnoreCase(name);
    }
}
