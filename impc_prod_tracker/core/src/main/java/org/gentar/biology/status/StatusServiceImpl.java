package org.gentar.biology.status;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    @Cacheable("allPlanStatuses")
    public List<Status> getAllStatuses()
    {
        return StreamSupport
                .stream(statusRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Cacheable("abortedPlanStatuses")
    public List<String> getAbortedPlanStatuses() {
        List<Status> statuses = getAllStatuses();
        return statuses
                .stream()
                .filter(Status::getIsAbortionStatus)
                .map(Status::getName)
                .collect(Collectors.toList());
    }


    @Cacheable("planStatusOrderingMap")
    public Map<String, Integer> getPlanStatusOrderingMap() {
        List<Status> statuses = getAllStatuses();
        return statuses
                .stream()
                .collect(Collectors.toMap(Status::getName, Status::getOrdering));
    }

}
