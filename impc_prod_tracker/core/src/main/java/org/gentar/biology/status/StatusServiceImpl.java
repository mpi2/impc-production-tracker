package org.gentar.biology.status;

import org.springframework.stereotype.Component;

@Component
public class StatusServiceImpl implements StatusService
{
    private StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository)
    {
        this.statusRepository = statusRepository;
    }

    public Status getStatusByName(String name)
    {
        return statusRepository.findByNameIgnoreCase(name);
    }
}
