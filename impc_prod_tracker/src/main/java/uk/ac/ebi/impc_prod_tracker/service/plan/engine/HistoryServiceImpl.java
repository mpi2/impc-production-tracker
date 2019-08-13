package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.history.History;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.history.HistoryRepository;
import java.util.List;

@Component
public class HistoryServiceImpl implements HistoryService
{
    private HistoryBuilder historyBuilder;
    private HistoryRepository historyRepository;
    private History historyEntry;

    public HistoryServiceImpl(HistoryBuilder historyBuilder, HistoryRepository historyRepository)
    {
        this.historyBuilder = historyBuilder;
        this.historyRepository = historyRepository;
    }

    @Override
    public void detectTrackOfChanges(Plan originalPlan, Plan newPlan)
    {
        historyEntry = historyBuilder.buildHistoryEntry(originalPlan, newPlan);
    }

    @Override
    public void saveTrackOfChanges()
    {
        if (historyEntry != null)
        {
            historyRepository.save(historyEntry);
        }
    }

    @Override
    public List<History> getHistoryByPlanId(Long planId)
    {
        return historyRepository.findAllByPlanIdOrderByDate(planId);
    }
}
