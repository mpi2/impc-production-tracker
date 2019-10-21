package uk.ac.ebi.impc_prod_tracker.web.mapping.allele;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.service.allele.AlleleService;

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
