package uk.ac.ebi.impc_prod_tracker.web.mapping.common.history;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import uk.ac.ebi.impc_prod_tracker.common.history.HistoryService;
import uk.ac.ebi.impc_prod_tracker.web.dto.common.history.HistoryDTO;
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
