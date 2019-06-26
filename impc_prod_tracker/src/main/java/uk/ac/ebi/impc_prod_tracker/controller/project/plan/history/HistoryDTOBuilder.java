package uk.ac.ebi.impc_prod_tracker.controller.project.plan.history;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;
import uk.ac.ebi.impc_prod_tracker.service.plan.engine.HistoryService;

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
        for (History historyEntry : historyEntries)
        {
            HistoryDTO historyDTO = new HistoryDTO();
            historyDTO.setAction(historyEntry.getAction());
            historyDTO.setDate(historyEntry.getDate());
            historyDTO.setUser(historyEntry.getUser());
            historyEntryDTOs.add(historyDTO);
        }
        return historyEntryDTOs;
    }
}
