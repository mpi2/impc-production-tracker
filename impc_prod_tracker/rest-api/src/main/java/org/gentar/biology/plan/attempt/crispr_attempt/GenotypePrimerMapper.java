package org.gentar.biology.plan.attempt.crispr_attempt;

import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.biology.plan.production.crispr_attempt.GenotypePrimerDTO;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenotypePrimerMapper implements Mapper<GenotypePrimer, GenotypePrimerDTO>
{
    private EntityMapper entityMapper;

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
        GenotypePrimer genotypePrimer =
            entityMapper.toTarget(genotypePrimerDTO, GenotypePrimer.class);
        removeInvalidId(genotypePrimer);
        return genotypePrimer;
    }

    public Set<GenotypePrimer> toEntities(Collection<GenotypePrimerDTO> genotypePrimerDTOS)
    {
        Set<GenotypePrimer> genotypePrimers =
            new HashSet<>(entityMapper.toTargets(genotypePrimerDTOS, GenotypePrimer.class));
        genotypePrimers.forEach(this::removeInvalidId);
        return genotypePrimers;
    }

    private void removeInvalidId(GenotypePrimer genotypePrimer)
    {
        if (genotypePrimer.getId() < 0)
        {
            genotypePrimer.setId(null);
        }
    }
}
