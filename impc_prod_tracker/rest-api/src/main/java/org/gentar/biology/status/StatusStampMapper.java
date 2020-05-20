package org.gentar.biology.status;

import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class to map any class implementing StatusStamp into a StatusStampsDTO.
 * The class does not implement {@link org.gentar.Mapper} because only the conversion to dto is
 * needed.
 */
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

    public List<StatusStampsDTO> toDtos (Set<? extends StatusStamp> statusStamps)
    {
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        if (statusStamps != null)
        {
            statusStamps.forEach(statusStamp -> statusStampsDTOS.add(toDto(statusStamp)));
        }
        return statusStampsDTOS;
    }
}

