package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangeEntry;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangesDetector;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity_;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan_;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates a History entry detecting the changes in an plan.
 */

@Component
public class HistoryBuilder
{
    private static final String TRANSITION_TEMPLATE = "field:%s|old:%s|new:%s";
    private SubjectRetriever subjectRetriever;
    private static final String RESOURCE_PRIVACY_PROPERTY_NAME = "resourcePrivacy";

    public HistoryBuilder(SubjectRetriever subjectRetriever)
    {
        this.subjectRetriever = subjectRetriever;
    }

    /**
     * Builds a {@link History} record with the information about the changes executed in a plan
     * as part as a update.
     * @param originalPlan The plan before the update.
     * @param newPlan The updated plan.
     * @return {@link History} object with the information or null if no changes where detected.
     */
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

    /**
     * Builds the action text for the changes. Separates each change with the symbol for lineSeparator.
     * @param originalPlan The plan before the update.
     * @param newPlan The updated plan.
     * @return Text describing the changes.
     */
    private String buildActionMessage(Plan originalPlan, Plan newPlan)
    {
        List<String> changes = getChanges(originalPlan, newPlan);
        return String.join(System.lineSeparator(), changes);
    }

    /**
     * Detects the changes between the plan before and after its change.
     * @param originalPlan The plan before the update.
     * @param newPlan The updated plan.
     * @return List of strings with the detected changes.
     */
    private List<String> getChanges(final Plan originalPlan, final Plan newPlan)
    {
        List<String> fieldsToExclude = new ArrayList<>();
        fieldsToExclude.add(BaseEntity_.CREATED_AT);
        fieldsToExclude.add(BaseEntity_.CREATED_BY);
        fieldsToExclude.add(BaseEntity_.LAST_MODIFIED);
        fieldsToExclude.add(BaseEntity_.LAST_MODIFIED_BY);
        fieldsToExclude.add(RESOURCE_PRIVACY_PROPERTY_NAME);
        List<String> fieldsToCheckRecursively = new ArrayList<>();
        fieldsToCheckRecursively.add(Plan_.CRISPR_ATTEMPT);

        ChangesDetector<Plan> changesDetector =
            new ChangesDetector<>(fieldsToExclude, originalPlan, newPlan);
        List<ChangeEntry> changeEntries = changesDetector.getChanges();

        List<String> changes =  new ArrayList<>();
        changeEntries.forEach(x ->
            changes.add(
                String.format(TRANSITION_TEMPLATE, x.getProperty(), x.getOldValue(), x.getNewValue())));

        return changes;
    }
}
