package org.gentar.biology.mutation.qc_results;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class QcTypeService
{
    private QcTypeRepository qcTypeRepository;

    public QcTypeService(QcTypeRepository qcTypeRepository)
    {
        this.qcTypeRepository = qcTypeRepository;
    }

    public QcType getQcTypeByName(String name)
    {
        return qcTypeRepository.findByNameIgnoreCase(name);
    }

    public QcType getQcTypeByNameFailsIfNull(String name)
    {
        QcType qcType = getQcTypeByName(name);
        if (qcType == null)
        {
            throw new UserOperationFailedException("Qc type [" + name + "] does not exist.");
        }
        return qcType;
    }
}
