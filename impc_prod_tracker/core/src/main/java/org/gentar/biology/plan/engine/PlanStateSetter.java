package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.status_stamp.PlanStatusStamp;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class PlanStateSetter
{
    private StatusService statusService;

    public PlanStateSetter(StatusService statusService)
    {
        this.statusService = statusService;
    }

    /**
     * Sets the status for a plan. It also record the stamp so there is historic information.
     * @param plan The plan to be updated.
     * @param statusName A string with the name of the status.
     */
    public void setStatusByName(Plan plan, String statusName)
    {
        Status newPlanStatus = statusService.getStatusByName(statusName);
        plan.setStatus(newPlanStatus);
        registerStatusStamp(plan);
    }

    private void registerStatusStamp(Plan plan)
    {
        Set<PlanStatusStamp> stamps = plan.getPlanStatusStamps();
        if (stamps == null)
        {
            stamps = new HashSet<>();
        }
        PlanStatusStamp planStatusStamp = new PlanStatusStamp();
        planStatusStamp.setPlan(plan);
        planStatusStamp.setStatus(plan.getStatus());
        planStatusStamp.setDate(LocalDateTime.now());
        stamps.add(planStatusStamp);
        plan.setPlanStatusStamps(stamps);
    }
}
