package org.gentar.biology.strain;

import org.springframework.stereotype.Component;

@Component
public class StrainServiceImpl implements StrainService
{
    private StrainRepository strainRepository;

    public StrainServiceImpl (StrainRepository strainRepository) { this.strainRepository = strainRepository; }

    @Override
    public Strain getStrainByName (String name) { return strainRepository.findByNameIgnoreCase(name); }

    @Override
    public Strain getStrainById (Long id) { return strainRepository.findById(id).orElse(null); }
}
