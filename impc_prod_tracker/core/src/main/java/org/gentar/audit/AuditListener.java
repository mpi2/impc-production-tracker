package org.gentar.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PreUpdate;
import org.gentar.audit.diff.ObjectIdExtractor;
import org.gentar.audit.history.HistoryService;
import org.gentar.util.BeanUtil;
import org.gentar.util.Cloner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gentar.audit.history.History;
import jakarta.transaction.Transactional;

import static jakarta.transaction.Transactional.TxType.MANDATORY;

/**
 * Class that keeps the logic after events in the entities are performed.
 */
public class AuditListener
{
    private Object originalObject;
    private Object newObject;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditListener.class);

    @PostLoad
    private void postLoad(Object object) throws JsonProcessingException {
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
        History history = historyService.detectTrackOfChanges(originalObject, newObject, id);
        if (history != null)
        {
            history.setComment(entityName + " updated");
            historyService.saveTrackOfChanges(history);
        }
    }
}
