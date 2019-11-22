package org.gentar.web.mapping.common.history;

import org.gentar.history.HistoryService;
import org.gentar.web.dto.common.history.HistoryDTO;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.Plan;
import org.gentar.history.History;

import java.util.List;

@Component
public class HistoryDTOBuilder
{
    private HistoryService<Plan> historyService;
    private HistoryMapper historyMapper;

    public HistoryDTOBuilder(HistoryService<Plan> historyService, HistoryMapper historyMapper)
    {
        this.historyService = historyService;
        this.historyMapper = historyMapper;
    }

    /**
     * Builds a list of HistoryDTO with the history for a plan.
     * @param plan The plan.
     * @return List of HistoryDTO.
     */
    public List<HistoryDTO> buildHistoryDTOBuilderFromPlan(Plan plan)
    {
        List<History> historyEntries =
            historyService.getHistoryByEntityNameAndEntityId("plan", plan.getId());
        return historyMapper.toDtos(historyEntries);
    }
}
