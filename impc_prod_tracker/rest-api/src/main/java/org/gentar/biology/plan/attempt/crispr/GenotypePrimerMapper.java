package org.gentar.biology.plan.attempt.crispr;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenotypePrimerMapper implements Mapper<GenotypePrimer, GenotypePrimerDTO>
{
    private final EntityMapper entityMapper;

    public GenotypePrimerMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    public GenotypePrimerDTO toDto(GenotypePrimer genotypePrimer)
    {
        return entityMapper.toTarget(genotypePrimer, GenotypePrimerDTO.class);
    }

    public List<GenotypePrimerDTO> toDtos(Collection<GenotypePrimer> genotypePrimers)
    {
        return entityMapper.toTargets(genotypePrimers, GenotypePrimerDTO.class);
    }

    public GenotypePrimer toEntity(GenotypePrimerDTO genotypePrimerDTO)
    {
        return entityMapper.toTarget(genotypePrimerDTO, GenotypePrimer.class);
    }

    public Set<GenotypePrimer> toEntities(Collection<GenotypePrimerDTO> genotypePrimerDTOS)
    {
        return new HashSet<>(entityMapper.toTargets(genotypePrimerDTOS, GenotypePrimer.class));
    }
}
