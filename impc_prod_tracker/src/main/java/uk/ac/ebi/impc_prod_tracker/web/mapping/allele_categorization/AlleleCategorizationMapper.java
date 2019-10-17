package uk.ac.ebi.impc_prod_tracker.web.mapping.allele_categorization;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_categorization.AlleleCategorization;
import uk.ac.ebi.impc_prod_tracker.web.dto.allele_categorization.AlleleCategorizationDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;
import java.util.Collection;
import java.util.List;

@Component
public class AlleleCategorizationMapper
{
    private EntityMapper entityMapper;

    public AlleleCategorizationMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    public AlleleCategorizationDTO toDto(AlleleCategorization alleleCategorization)
    {
        return entityMapper.toTarget(alleleCategorization, AlleleCategorizationDTO.class);
    }

    public List<AlleleCategorizationDTO> toDtos(Collection<AlleleCategorization> alleleCategorizations)
    {
        return entityMapper.toTargets(alleleCategorizations, AlleleCategorizationDTO.class);
    }
}
