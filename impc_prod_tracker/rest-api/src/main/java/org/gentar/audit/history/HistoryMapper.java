package org.gentar.audit.history;

import org.gentar.Mapper;
import org.gentar.common.history.HistoryDTO;
import org.gentar.common.history.HistoryDetailDTO;
import org.springframework.stereotype.Component;
import org.gentar.audit.history.detail.HistoryDetail;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HistoryMapper implements Mapper<History, HistoryDTO>
{
    @Override
    public HistoryDTO toDto(History history)
    {
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId(1);
        if (history != null)
        {
            historyDTO.setComment(history.getComment());
            historyDTO.setDate(history.getDate());
            historyDTO.setUser(history.getUser());

            historyDTO.setDetails(buildHistoryDetails(history.getHistoryDetailSet()));
        }
        return historyDTO;
    }
    @Override
    public List<HistoryDTO> toDtos(Collection<History> histories)
    {
        List<HistoryDTO> historyDTOS = Mapper.super.toDtos(histories);
        AtomicInteger counter = new AtomicInteger(1);
        historyDTOS.forEach(x -> x.setId(counter.getAndIncrement()));
        return historyDTOS;
    }

    private List<HistoryDetailDTO> buildHistoryDetails(Collection<HistoryDetail> details)
    {
        List<HistoryDetailDTO> detailDTOs = new ArrayList<>();
        details.forEach(
            x-> detailDTOs.add(
                new HistoryDetailDTO(x.getField(), x.getOldValue(), x.getNewValue(), x.getNote())));
        return detailDTOs;
    }
}
