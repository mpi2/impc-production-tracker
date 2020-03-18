package org.gentar.biology.status;

import org.gentar.Mapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.assignment_status.AssignmentStatusStamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class StatusStampMapper implements Mapper<AssignmentStatusStamp, StatusStampsDTO>
{
    public StatusStampsDTO toDto (AssignmentStatusStamp assignmentStatusStamp)
    {
        StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
        statusStampsDTO.setStatusName(assignmentStatusStamp.getAssignmentStatus().getName());
        statusStampsDTO.setDate(assignmentStatusStamp.getDate());
        return statusStampsDTO;
    }

    public List<StatusStampsDTO> toDtos (Set<AssignmentStatusStamp> assignmentStatusStamps)
    {
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        assignmentStatusStamps.forEach(assignmentStatusStamp -> statusStampsDTOS.add(toDto(assignmentStatusStamp)));
        return statusStampsDTOS;
    }
}
