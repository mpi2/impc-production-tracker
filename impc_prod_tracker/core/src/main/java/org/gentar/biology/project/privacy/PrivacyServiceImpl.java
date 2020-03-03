package org.gentar.biology.project.privacy;

import org.springframework.stereotype.Component;

@Component
public class PrivacyServiceImpl implements PrivacyService
{
    private PrivacyRepository privacyRepository;

    public PrivacyServiceImpl(PrivacyRepository privacyRepository)
    {
        this.privacyRepository = privacyRepository;
    }

    public Privacy findPrivacyByName(String name)
    {
        return privacyRepository.findByNameIgnoreCase(name);
    }
}
