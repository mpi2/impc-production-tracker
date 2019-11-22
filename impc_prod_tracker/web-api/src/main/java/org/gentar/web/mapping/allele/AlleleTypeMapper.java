package org.gentar.web.mapping.allele;

import org.gentar.service.biology.allele.AlleleService;
import org.springframework.stereotype.Component;
import org.gentar.biology.allele_type.AlleleType;

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
