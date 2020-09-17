package org.gentar.biology.plan.attempt;

import org.gentar.biology.plan.type.PlanTypeName;

import java.util.List;
import java.util.Map;

public interface AttemptTypeService
{
    List<AttemptType> getAll();
    AttemptType getAttemptTypeByName(String attemptTypeName);
    List<AttemptTypesName> getAttemptTypesByPlanTypeName(PlanTypeName planTypeName);
    Map<String, List<String>> getAttemptTypesByPlanTypeNameMap();
}
