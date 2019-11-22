package org.gentar.service.biology.allele;

import org.springframework.stereotype.Component;
import org.gentar.biology.allele_type.AlleleType;
import org.gentar.biology.allele_type.AlleleTypeRepository;

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
