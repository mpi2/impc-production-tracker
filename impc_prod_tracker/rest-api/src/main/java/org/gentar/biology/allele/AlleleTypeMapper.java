package org.gentar.biology.allele;

import org.gentar.biology.allele.AlleleService;
import org.springframework.stereotype.Component;
import org.gentar.biology.allele.AlleleType;

@Component
public class AlleleTypeMapper
{
    private AlleleService alleleService;

    public AlleleTypeMapper(AlleleService alleleService)
    {
        this.alleleService = alleleService;
    }

    public AlleleType toEntity(String alleleTypeName)
    {
        return alleleService.getAlleleTypeByName(alleleTypeName);
    }
}
