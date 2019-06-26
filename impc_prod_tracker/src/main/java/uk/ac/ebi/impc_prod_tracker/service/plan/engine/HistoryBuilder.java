package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryBuilder
{
    private static final String TRANSITION_TEMPLATE = "%s from [%s] to [%s]";
    private SubjectRetriever subjectRetriever;

    public HistoryBuilder(SubjectRetriever subjectRetriever)
    {
        this.subjectRetriever = subjectRetriever;
    }

    public History buildHistoryEntry(Plan originalPlan, Plan newPlan)
    {
        History history = new History();
        String actionsMessage = buildActionMessage(originalPlan, newPlan);
        if (actionsMessage.isEmpty())
        {
            history = null;
        }
        else
        {
            history.setPlan(newPlan);
            history.setDate(LocalDateTime.now());

            history.setAction(actionsMessage);
            history.setUser(subjectRetriever.getSubject().getLogin());
        }

        return history;
    }

    private String buildActionMessage(Plan originalPlan, Plan newPlan)
    {
        List<String> changes = getChanges(originalPlan, newPlan);
        StringBuilder changesSb = new StringBuilder();
        for (String change : changes)
        {
            changesSb.append(change).append("\n");
        }
        return changesSb.toString();
    }

    private List<String> getChanges(final Plan originalPlan, final Plan newPlan)
    {
        List<String> changes =  new ArrayList<>();

        if (originalPlan.getPrivacy() != newPlan.getPrivacy())
        {
            String originalPrivacy = "";
            String newPrivacy = "";
            if (originalPlan.getPrivacy() != null)
            {
                originalPrivacy = originalPlan.getPrivacy().getName();
            }
            if (newPlan.getPrivacy() != null)
            {
                newPrivacy = newPlan.getPrivacy().getName();
            }
            if (!originalPrivacy.equals(newPrivacy))
            {
                changes.add(
                    String.format(TRANSITION_TEMPLATE, "Privacy", originalPrivacy, newPrivacy));
            }
        }

        return changes;
    }
}
