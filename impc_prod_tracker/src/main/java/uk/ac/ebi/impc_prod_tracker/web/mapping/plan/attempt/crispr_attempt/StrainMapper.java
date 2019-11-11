package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.StrainRepository;
import uk.ac.ebi.impc_prod_tracker.web.dto.strain.StrainDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;

@Component
public class StrainMapper
{
    private EntityMapper entityMapper;
    private StrainRepository strainRepository;

    public StrainMapper(EntityMapper entityMapper, StrainRepository strainRepository)
    {
        this.entityMapper = entityMapper;
        this.strainRepository = strainRepository;
    }

    public Strain toEntity(StrainDTO strainDTO)
    {
        Strain strain = entityMapper.toTarget(strainDTO, Strain.class);
        if (strain != null && strain.getStrainTypes() == null && strain.getId() != null)
        {
            Strain persisted = strainRepository.findById(strain.getId()).orElse(null);
            if (persisted != null)
            {
                strain.setStrainTypes(persisted.getStrainTypes());
            }

        }
        return strain;
    }
}
