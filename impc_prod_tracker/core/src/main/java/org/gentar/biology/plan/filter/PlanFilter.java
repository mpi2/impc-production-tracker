package org.gentar.biology.plan.filter;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PlanFilter
{
    private Map<PlanFilterType, List<String>> filters;

    PlanFilter()
    {
        filters = new HashMap<>();
    }

    public static final PlanFilter getInstance()
    {
        return new PlanFilter();
    }

    public List<String> getTpns()
    {
        return filters.getOrDefault(PlanFilterType.TPN, null);
    }

    public List<String> getWorkUnitNames()
    {
        return filters.getOrDefault(PlanFilterType.WORK_UNIT_NAME, null);
    }

    public List<String> getWorGroupNames()
    {
        return filters.getOrDefault(PlanFilterType.WORK_GROUP_NAME, null);
    }

    public List<String> getStatusNames()
    {
        return filters.getOrDefault(PlanFilterType.STATUS_NAME, null);
    }

    public List<String> getPins()
    {
        return filters.getOrDefault(PlanFilterType.PIN, null);
    }

    public List<String> getPlanTypeNames()
    {
        return filters.getOrDefault(PlanFilterType.PLAN_TYPE_NAME, null);
    }

    public List<String> getAttemptTypeNames()
    {
        return filters.getOrDefault(PlanFilterType.ATTEMPT_TYPE_NAME, null);
    }

    public List<String> getSummaryStatusNames()
    {
        return filters.getOrDefault(PlanFilterType.SUMMARY_STATUS_NAME, null);
    }

    public List<String> getImitsMiAttemptIds()
    {
        return filters.getOrDefault(PlanFilterType.IMITS_MI_PLAN, null);
    }

    public List<String> getImitsPhenotypeAttemptIds()
    {
        return filters.getOrDefault(PlanFilterType.IMITS_PHENITYPING_ATTEMPT, null);
    }

    public List<String> getPhenotypingExternalRefs () { return filters.getOrDefault(PlanFilterType.PHENOTYPING_EXTERNAL_REF, null); }
}
