package org.gentar.biology.status;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StatusServiceImpl implements StatusService
{
    private StatusRepository statusRepository;
    private static final String STATUS_NOT_FOUND_ERROR = "Status [%s] not found";

    public StatusServiceImpl(StatusRepository statusRepository)
    {
        this.statusRepository = statusRepository;
    }

    @Cacheable("statuses")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Status getStatusByName(String name)
    {
        return statusRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Status getStatusByNameFailWhenNotFound(String name)
    {
        Status status = getStatusByName(name);
        if (status == null)
        {
            throw new UserOperationFailedException(String.format(STATUS_NOT_FOUND_ERROR, name));
        }
        return status;
    }
}
