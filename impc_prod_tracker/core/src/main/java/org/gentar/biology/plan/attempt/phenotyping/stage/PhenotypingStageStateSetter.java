package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.StateSetter;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class PhenotypingStageStateSetter implements StateSetter
{
    private StatusService statusService;

    public PhenotypingStageStateSetter(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public void setStatus(ProcessData entity, Status status)
    {
        entity.setStatus(status);
        registerStatusStamp((PhenotypingStage)entity);
    }

    @Override
    public void setStatusByName(ProcessData entity, String statusName)
    {
        Status newPlanStatus = statusService.getStatusByName(statusName);
        setStatus(entity, newPlanStatus);
    }

    private void registerStatusStamp(PhenotypingStage phenotypingStage)
    {
        Set<PhenotypingStageStatusStamp> stamps = phenotypingStage.getPhenotypingStageStatusStamps();
        if (stamps == null)
        {
            stamps = new HashSet<>();
        }
        PhenotypingStageStatusStamp phenotypingStageStatusStamp = new PhenotypingStageStatusStamp();
        phenotypingStageStatusStamp.setPhenotypingStage(phenotypingStage);
        phenotypingStageStatusStamp.setStatus(phenotypingStage.getStatus());
        phenotypingStageStatusStamp.setDate(LocalDateTime.now());
        stamps.add(phenotypingStageStatusStamp);
        phenotypingStage.setPhenotypingStageStatusStamps(stamps);
    }
}
