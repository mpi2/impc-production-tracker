package org.gentar.biology.plan.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.tissue_distribution.TissueDistribution;
import org.springframework.stereotype.Component;

@Component
public class TissueDistributionMapper implements Mapper<TissueDistribution, TissueDistributionDTO>
{
    private EntityMapper entityMapper;

    public TissueDistributionMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public TissueDistributionDTO toDto(TissueDistribution entity)
    {
        return entityMapper.toTarget(entity, TissueDistributionDTO.class);
    }

    @Override
    public TissueDistribution toEntity(TissueDistributionDTO dto)
    {
        return entityMapper.toTarget(dto, TissueDistribution.class);
    }
}
