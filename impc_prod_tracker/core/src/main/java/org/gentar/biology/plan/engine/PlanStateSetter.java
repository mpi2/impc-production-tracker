package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.status.PlanStatusStamp;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusNames;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.StateSetter;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Any class wanting to set the status in a plan needs to call this class to do that.
 * This class sets the new status and registers the action in the stamp table for plan statuses.
 */
@Component
public class PlanStateSetter implements StateSetter
{
    private StatusService statusService;

    public PlanStateSetter(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public void setStatus(ProcessData entity, Status status)
    {
        entity.setProcessDataStatus(status);
        registerStatusStamp((Plan)entity);
    }

    /**
     * Sets the status for a plan. It also record the stamp so there is historic information.
     * @param entity The entity to be updated.
     * @param statusName A string with the name of the status.
     */
    public void setStatusByName(ProcessData entity, String statusName)
    {
        Status newPlanStatus = statusService.getStatusByNameFailWhenNotFound(statusName);
        setStatus(entity, newPlanStatus);
    }

    @Override
    public void setInitialStatus(ProcessData entity)
    {
        setStatusByName(entity, StatusNames.PLAN_CREATED);
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
