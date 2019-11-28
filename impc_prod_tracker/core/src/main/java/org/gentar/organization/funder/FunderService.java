package org.gentar.organization.funder;

import org.springframework.stereotype.Service;

@Service
public class FunderService
{
    private FunderRepository funderRepository;

    public FunderService(FunderRepository funderRepository)
    {
        this.funderRepository = funderRepository;
    }

    public Funder getFunderByName(String name)
    {
        return funderRepository.getFirstByNameIgnoreCase(name);
    }
}
