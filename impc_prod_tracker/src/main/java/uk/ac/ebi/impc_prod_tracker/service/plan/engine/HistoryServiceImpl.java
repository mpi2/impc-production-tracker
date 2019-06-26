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

    public HistoryServiceImpl(HistoryBuilder historyBuilder, HistoryRepository historyRepository)
    {
        this.historyBuilder = historyBuilder;
        this.historyRepository = historyRepository;
    }

    @Override
    public void traceChanges(Plan originalPlan, Plan newPlan)
    {
        History historyEntry = historyBuilder.buildHistoryEntry(originalPlan, newPlan);
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
