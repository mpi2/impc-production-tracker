package org.gentar.organization.funder;

import org.springframework.stereotype.Component;

@Component
public class FunderServiceImpl implements FunderService
{
    private FunderRepository funderRepository;

    public FunderServiceImpl(FunderRepository funderRepository)
    {
        this.funderRepository = funderRepository;
    }

    public Funder getFunderByName(String name)
    {
        return funderRepository.getFirstByNameIgnoreCase(name);
    }
}
