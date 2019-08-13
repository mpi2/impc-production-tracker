package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.history;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.history.History;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.history.detail.HistoryDetail;
import uk.ac.ebi.impc_prod_tracker.service.plan.engine.HistoryService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.history.HistoryDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.history.HistoryDetailDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryDTOBuilder
{
    private HistoryService historyService;

    public HistoryDTOBuilder(HistoryService historyService)
    {
        this.historyService = historyService;
    }

    /**
     * Builds a list of HistoryDTO with the history for a plan.
     * @param plan The plan.
     * @return List of HistoryDTO.
     */
    public List<HistoryDTO> buildHistoryDTOBuilderFromPlan(Plan plan)
    {
        List<History> historyEntries = historyService.getHistoryByPlanId(plan.getId());
        List<HistoryDTO> historyEntryDTOs = new ArrayList<>();
        int counter = 1;
        for (History historyEntry : historyEntries)
        {
            HistoryDTO historyDTO = new HistoryDTO();
            historyDTO.setId(counter++);
            historyDTO.setComment(historyEntry.getComment());
            historyDTO.setDate(historyEntry.getDate());
            historyDTO.setUser(historyEntry.getUser());

            historyDTO.setDetails(buildHistoryDetails(historyEntry.getHistoryDetailSet()));

            historyEntryDTOs.add(historyDTO);
        }
        return historyEntryDTOs;
    }

    private List<HistoryDetailDTO> buildHistoryDetails(List<HistoryDetail> details)
    {
        List<HistoryDetailDTO> detailDTOs = new ArrayList<>();
        details.forEach(
            x-> detailDTOs.add(
                new HistoryDetailDTO(x.getField(), x.getOldValue(), x.getNewValue())));
        return detailDTOs;
    }
}
