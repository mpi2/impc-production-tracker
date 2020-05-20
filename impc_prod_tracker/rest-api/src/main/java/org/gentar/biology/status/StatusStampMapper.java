package org.gentar.biology.status;

import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class StatusStampMapper
{
    public StatusStampsDTO toDto(StatusStamp statusStamp)
    {
        StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
        statusStampsDTO.setStatusName(statusStamp.getStatusName());
        statusStampsDTO.setDate(statusStamp.getDate());
        return statusStampsDTO;
    }

    public List<StatusStampsDTO> toDtos ( Set<? extends StatusStamp> statusStamps)
    {
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        statusStamps.forEach(statusStamp -> statusStampsDTOS.add(toDto(statusStamp)));
        return statusStampsDTOS;
    }
}
