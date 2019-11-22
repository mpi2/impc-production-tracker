package org.gentar.web.mapping.statusStamp;

import org.gentar.web.dto.status_stamps.StatusStampsDTO;
import org.springframework.stereotype.Component;
import org.gentar.biology.assignment_status_stamp.AssignmentStatusStamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class StatusStampMapper
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
