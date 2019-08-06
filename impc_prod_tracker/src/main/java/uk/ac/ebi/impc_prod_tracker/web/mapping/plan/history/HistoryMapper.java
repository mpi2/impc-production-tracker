package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.history;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.detail.HistoryDetail;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.history.HistoryDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.history.HistoryDetailDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class HistoryMapper
{
    public HistoryDTO toDto(History history)
    {
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId(1);
        historyDTO.setComment(history.getComment());
        historyDTO.setDate(history.getDate());
        historyDTO.setUser(history.getUser());

        historyDTO.setDetails(buildHistoryDetails(history.getHistoryDetailSet()));

        return historyDTO;
    }

    public List<HistoryDTO> toDtos(List<History> histories)
    {
        int counter = 1;
        List<HistoryDTO> historyEntryDTOs = new ArrayList<>();
        for (History historyEntry : histories)
        {
            HistoryDTO historyDTO = toDto(historyEntry);
            historyDTO.setId(counter++);
            historyEntryDTOs.add(historyDTO);
        }
        return historyEntryDTOs;
    }

    private List<HistoryDetailDTO> buildHistoryDetails(Collection<HistoryDetail> details)
    {
        List<HistoryDetailDTO> detailDTOs = new ArrayList<>();
        details.forEach(
            x-> detailDTOs.add(
                new HistoryDetailDTO(x.getField(), x.getOldValue(), x.getNewValue())));
        return detailDTOs;
    }
}
