package org.gentar.biology.plan.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanFilterBuilder
{
    private Map<PlanFilterType, List<String>> filters;

    // Prevents instantiation.
    private PlanFilterBuilder()
    {
        filters = new HashMap<>();
    }

    public static PlanFilterBuilder getInstance()
    {
        return new PlanFilterBuilder();
    }

    public PlanFilter build()
    {
        PlanFilter planFilter = new PlanFilter();
        planFilter.setFilters(filters);
        return planFilter;
    }

    private PlanFilterBuilder withFilter(PlanFilterType filterType, List<String> values)
    {
        if (isListValid(values))
        {
            filters.put(filterType, values);
        }
        return this;
    }

    public PlanFilterBuilder withTpns(List<String> tpns)
    {
        return withFilter(PlanFilterType.TPN, tpns);
    }

    public PlanFilterBuilder withWorkUnitNames(List<String> workUnitNames)
    {
        return withFilter(PlanFilterType.WORK_UNIT_NAME, workUnitNames);
    }

    public PlanFilterBuilder withWorkGroupNames(List<String> workGroupNames)
    {
        return withFilter(PlanFilterType.WORK_GROUP_NAME, workGroupNames);
    }

    public PlanFilterBuilder withStatusNames(List<String> statusNames)
    {
        return withFilter(PlanFilterType.STATUS_NAME, statusNames);
    }

    public PlanFilterBuilder withPins(List<String> pins)
    {
        return withFilter(PlanFilterType.PIN, pins);
    }

    public PlanFilterBuilder withPlanTypeNames(List<String> planTypeNames)
    {
        return withFilter(PlanFilterType.PLAN_TYPE_NAME, planTypeNames);
    }

    public PlanFilterBuilder withAttemptTypeNames(List<String> attemptTypeNames)
    {
        return withFilter(PlanFilterType.ATTEMPT_TYPE_NAME, attemptTypeNames);
    }

    public PlanFilterBuilder withSummaryStatusNames(List<String> summaryStatusNames)
    {
        return withFilter(PlanFilterType.SUMMARY_STATUS_NAME, summaryStatusNames);
    }

    public PlanFilterBuilder withImitsMiAttemptIds(List<String> imitsMiAttemptIds)
    {
        return withFilter(PlanFilterType.IMITS_MI_PLAN, imitsMiAttemptIds);
    }

    public PlanFilterBuilder withImitsPhenotypeAttemptIds(List<String> imitsPhenotypeAttemptIds)
    {
        return withFilter(PlanFilterType.IMITS_PHENITYPING_ATTEMPT, imitsPhenotypeAttemptIds);
    }

    private boolean isListValid(List<String> values)
    {
        return values != null && !values.isEmpty();
    }
}
