package org.gentar.biology.allele;

import org.springframework.stereotype.Component;

@Component
public class AlleleService
{
    private AlleleTypeRepository alleleTypeRepository;

    public AlleleService(AlleleTypeRepository alleleTypeRepository)
    {
        this.alleleTypeRepository = alleleTypeRepository;
    }

    public AlleleType getAlleleTypeByName(String alleleTypeName)
    {
        return alleleTypeRepository.findFirstByNameIgnoreCase(alleleTypeName);
    }
}
