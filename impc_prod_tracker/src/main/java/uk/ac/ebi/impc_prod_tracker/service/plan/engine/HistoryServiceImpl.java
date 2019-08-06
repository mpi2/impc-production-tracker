package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.HistoryRepository;
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
    public History detectTrackOfChanges(Plan originalPlan, Plan newPlan)
    {
        return historyBuilder.buildHistoryEntry(originalPlan, newPlan);
    }

    @Override
    public void saveTrackOfChanges(History historyEntry)
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
