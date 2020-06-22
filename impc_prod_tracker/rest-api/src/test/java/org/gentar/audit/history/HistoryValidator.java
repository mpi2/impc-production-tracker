package org.gentar.audit.history;

import org.gentar.common.history.HistoryDetailDTO;
import java.util.List;


public class HistoryValidator
{
    public static HistoryDetailDTO getHistoryDetailByField(
        List<HistoryDetailDTO> historyDetailDTOS, String field)
    {
        HistoryDetailDTO historyDetailDTO = null;
        if (historyDetailDTOS != null)
        {
            historyDetailDTO = historyDetailDTOS.stream()
                .filter(x -> x.getField().equals(field))
                .findFirst().orElse(null);
        }
        return historyDetailDTO;
    }
}
