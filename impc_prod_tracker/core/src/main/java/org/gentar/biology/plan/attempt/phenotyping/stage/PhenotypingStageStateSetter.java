package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.EarlyAdultState;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.PhenotypingStageState;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeName;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.StateSetter;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class PhenotypingStageStateSetter implements StateSetter
{
    private StatusService statusService;
    private static final Map<PhenotypingStageTypeName, String> SUFFIXES;

    static
    {
        SUFFIXES = new HashMap<>();
        SUFFIXES.put(PhenotypingStageTypeName.EARLY_ADULT, "");
        SUFFIXES.put(PhenotypingStageTypeName.LATE_ADULT, "Late Adult");
        SUFFIXES.put(PhenotypingStageTypeName.HAPLOESSENTIAL, "Haplo-Essential");
        SUFFIXES.put(PhenotypingStageTypeName.EMBRYO, "Embryo");
    }

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
        String specificStatusName = getSpecificStatusName((PhenotypingStage) entity, statusName);
        Status newPlanStatus = statusService.getStatusByName(specificStatusName);
        setStatus(entity, newPlanStatus);
    }

    @Override
    public void setInitialStatus(ProcessData entity)
    {
        PhenotypingStageType phenotypingStageType =
            ((PhenotypingStage) entity).getPhenotypingStageType();
        String initialStatusName;
        if (isEarlyAdult(phenotypingStageType))
        {
            initialStatusName =
                EarlyAdultState.PhenotypingProductionRegistered.getInternalName();
        }
        else
        {
            initialStatusName =
                PhenotypingStageState.PhenotypingProductionRegistered.getInternalName();
        }
        setStatusByName(entity, initialStatusName);
    }

    /**
     * Late Adult, haplo-essential and embryo state machines share a single state machine with
     * generic statuses names, so the final name of the status needs some transformation before
     * it can match the status in the database. Only Early Adult has already in its definition the
     * final names for the statuses.
     * @param statusName The name of the status given by the state machine.
     * @return The name of the status that should match one in the database.
     */
    String getSpecificStatusName(PhenotypingStage phenotypingStage, String statusName)
    {
        String finalStatusName = statusName;
        PhenotypingStageType phenotypingStageType = phenotypingStage.getPhenotypingStageType();
        if (!isEarlyAdult(phenotypingStageType))
        {
            PhenotypingStageTypeName phenotypingStageTypeName =
                PhenotypingStageTypeName.valueOfLabel(phenotypingStageType.getName());
            finalStatusName = SUFFIXES.get(phenotypingStageTypeName) + " " + statusName;
        }
        return finalStatusName;
    }

    private boolean isEarlyAdult(PhenotypingStageType phenotypingStageType)
    {
        return PhenotypingStageTypeName.EARLY_ADULT.getLabel().equals(phenotypingStageType.getName());
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
