package uk.ac.ebi.impc_prod_tracker.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.impc_prod_tracker.common.ObjectIdExtractor;
import uk.ac.ebi.impc_prod_tracker.common.cloning.Cloner;
import uk.ac.ebi.impc_prod_tracker.common.history.HistoryService;
import uk.ac.ebi.impc_prod_tracker.data.common.history.History;
import javax.persistence.PostLoad;
import javax.transaction.Transactional;
import javax.persistence.PreUpdate;
import java.lang.reflect.Method;

import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * Class that keeps the logic after events in the entities are performed.
 */
public class AuditListener
{
    private Object originalObject;
    private Object newObject;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditListener.class);

    @PostLoad
    private void postLoad(Object object)
    {
        originalObject = Cloner.cloneThroughJson(object);
    }

    @PreUpdate
    private void preUpdate(Object object)
    {
        newObject = object;
        saveUpdateRecordInHistory();
    }

    @Transactional(MANDATORY)
    void saveUpdateRecordInHistory()
    {
        long id = ObjectIdExtractor.getObjectId(originalObject);
        HistoryService historyService = BeanUtil.getBean(HistoryService.class);
        String entityName = originalObject.getClass().getSimpleName();
        historyService.setEntityData(entityName, id);
        History history = historyService.detectTrackOfChanges(originalObject, newObject);
        if (history != null)
        {
            history.setComment(entityName + " updated");
            historyService.saveTrackOfChanges(history);
        }
    }
}
