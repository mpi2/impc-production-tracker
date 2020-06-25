package org.gentar.biology.mutation.qc_results;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class QcStatusService
{
    private QcStatusRepository qcStatusRepository;

    public QcStatusService(QcStatusRepository qcStatusRepository)
    {
        this.qcStatusRepository = qcStatusRepository;
    }

    public QcStatus getQcStatusByName(String name)
    {
        return qcStatusRepository.findByNameIgnoreCase(name);
    }

    public QcStatus getQcStatusByNameFailsIfNull(String name)
    {
        QcStatus qcStatus = getQcStatusByName(name);
        if (qcStatus == null)
        {
            throw new UserOperationFailedException("Qc status [" + name + "] does not exist.");
        }
        return qcStatus;
    }
}
