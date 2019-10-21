package uk.ac.ebi.impc_prod_tracker.service.allele;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleTypeRepository;

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
