package org.gentar.biology.specimen.engine;

import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.specimen.status_stamp.SpecimenStatusStamp;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.StateSetter;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class SpecimenStateSetter implements StateSetter
{
    private StatusService statusService;

    public SpecimenStateSetter(StatusService statusService)
    {
        this.statusService = statusService;
    }

    @Override
    public void setStatus(ProcessData entity, Status status)
    {
        entity.setProcessDataStatus(status);
        registerStatusStamp((Specimen)entity);
    }

    @Override
    public void setStatusByName(ProcessData entity, String statusName)
    {
        Status newStatus = statusService.getStatusByName(statusName);
        setStatus(entity, newStatus);
    }

    @Override
    public void setInitialStatus(ProcessData entity)
    {
        setStatusByName(entity, SpecimenState.SpecimenGroupInProgress.getInternalName());
    }

    private void registerStatusStamp(Specimen Specimen)
    {
        Set<SpecimenStatusStamp> stamps = Specimen.getSpecimenStatusStamps();
        if (stamps == null)
        {
            stamps = new HashSet<>();
        }
        SpecimenStatusStamp SpecimenStatusStamp = new SpecimenStatusStamp();
        SpecimenStatusStamp.setSpecimen(Specimen);
        SpecimenStatusStamp.setStatus(Specimen.getProcessDataStatus());
        SpecimenStatusStamp.setDate(LocalDateTime.now());
        stamps.add(SpecimenStatusStamp);
        Specimen.setSpecimenStatusStamps(stamps);
    }

}
