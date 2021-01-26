package org.gentar.biology.status;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class StatusServiceImpl implements StatusService
{
    private final StatusRepository statusRepository;
    private static final String STATUS_NOT_FOUND_ERROR = "Status [%s] not found";

    public StatusServiceImpl(StatusRepository statusRepository)
    {
        this.statusRepository = statusRepository;
    }

    @Override
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

    public List<Status> getAllStatuses()
    {
        return StreamSupport
                .stream(statusRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
