package org.gentar.biology.colony.engine;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.status_stamp.ColonyStatusStamp;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.StateSetter;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class ColonyStateSetter implements StateSetter
{
    private StatusService statusService;

    public ColonyStateSetter(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public void setStatus(ProcessData entity, Status status)
    {
        entity.setStatus(status);
        registerStatusStamp((Colony)entity);
    }

    @Override
    public void setStatusByName(ProcessData entity, String statusName)
    {
        Status newPlanStatus = statusService.getStatusByName(statusName);
        setStatus(entity, newPlanStatus);
    }

    private void registerStatusStamp(Colony colony)
    {
        Set<ColonyStatusStamp> stamps = colony.getColonyStatusStamps();
        if (stamps == null)
        {
            stamps = new HashSet<>();
        }
        ColonyStatusStamp colonyStatusStamp = new ColonyStatusStamp();
        colonyStatusStamp.setColony(colony);
        colonyStatusStamp.setStatus(colony.getStatus());
        colonyStatusStamp.setDate(LocalDateTime.now());
        stamps.add(colonyStatusStamp);
        colony.setColonyStatusStamps(stamps);
    }
}
