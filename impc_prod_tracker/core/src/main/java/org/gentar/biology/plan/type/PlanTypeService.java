package org.gentar.biology.plan.type;

public interface PlanTypeService
{
    PlanType getPlanTypeByName(String name);
    PlanType getPlanTypeByNameFailsIfNull(String name);
}
