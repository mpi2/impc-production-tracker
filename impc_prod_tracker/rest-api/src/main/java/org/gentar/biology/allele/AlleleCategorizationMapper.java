package org.gentar.biology.allele;

import org.springframework.stereotype.Component;
import org.gentar.biology.allele.categorizarion.AlleleCategorization;
import org.gentar.biology.allele.AlleleCategorizationDTO;
import org.gentar.EntityMapper;
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
