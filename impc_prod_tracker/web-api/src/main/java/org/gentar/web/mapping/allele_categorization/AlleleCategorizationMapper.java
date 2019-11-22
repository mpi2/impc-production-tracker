package org.gentar.web.mapping.allele_categorization;

import org.springframework.stereotype.Component;
import org.gentar.biology.allele_categorization.AlleleCategorization;
import org.gentar.web.dto.allele_categorization.AlleleCategorizationDTO;
import org.gentar.web.mapping.EntityMapper;
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
