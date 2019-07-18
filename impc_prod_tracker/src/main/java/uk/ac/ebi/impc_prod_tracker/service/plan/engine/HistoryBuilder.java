package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangeEntry;
import uk.ac.ebi.impc_prod_tracker.common.diff.ChangesDetector;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity_;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.History;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history.detail.HistoryDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates a History entry detecting the changes in an plan.
 */

@Component
public class HistoryBuilder
{
    private SubjectRetriever subjectRetriever;
    private static final String RESOURCE_PRIVACY_PROPERTY_NAME = "resourcePrivacy";
    private static final String RESTRICTED_OBJECT_PROPERTY_NAME = "restrictedObject";


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
        List<HistoryDetail> historyDetails = getDetails(originalPlan, newPlan);
        if (historyDetails.isEmpty())
        {
            history = null;
        }
        else
        {
            history.setPlan(newPlan);
            history.setDate(LocalDateTime.now());

            history.setHistoryDetailSet(historyDetails);
            history.setUser(subjectRetriever.getSubject().getLogin());
        }

        return history;
    }

    /**
     * Detects the changes between the plan before and after its change.
     * @param originalPlan The plan before the update.
     * @param newPlan The updated plan.
     * @return List of strings with the detected changes.
     */
    private List<HistoryDetail> getDetails(final Plan originalPlan, final Plan newPlan)
    {
        List<String> fieldsToExclude = new ArrayList<>();
        fieldsToExclude.add(BaseEntity_.CREATED_AT);
        fieldsToExclude.add(BaseEntity_.CREATED_BY);
        fieldsToExclude.add(BaseEntity_.LAST_MODIFIED);
        fieldsToExclude.add(BaseEntity_.LAST_MODIFIED_BY);
        fieldsToExclude.add(RESOURCE_PRIVACY_PROPERTY_NAME);
        fieldsToExclude.add(RESTRICTED_OBJECT_PROPERTY_NAME);
        fieldsToExclude.add("id");

        ChangesDetector<Plan> changesDetector =
            new ChangesDetector<>(fieldsToExclude, originalPlan, newPlan);
        List<ChangeEntry> changeEntries = changesDetector.getChanges();

        List<HistoryDetail> details =  new ArrayList<>();
        changeEntries.forEach(x ->
            {
                HistoryDetail historyDetail = new HistoryDetail();
                historyDetail.setField(x.getProperty());
                historyDetail.setOldValue(x.getOldValue());
                historyDetail.setNewValue(x.getNewValue());

                details.add(historyDetail);
            });

        return details;
    }
}
