package org.gentar.biology.status;

import org.springframework.stereotype.Component;

@Component
public class StatusService
{
    private StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository)
    {
        this.statusRepository = statusRepository;
    }

    public Status getStatusByName(String name)
    {
        return statusRepository.findByNameIgnoreCase(name);
    }
}
