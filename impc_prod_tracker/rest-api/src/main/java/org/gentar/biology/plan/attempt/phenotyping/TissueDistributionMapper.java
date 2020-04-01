package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.springframework.stereotype.Component;

import java.util.*;

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
    public List<TissueDistributionDTO> toDtos(Collection<TissueDistribution> tissueDistributions)
    {
        List<TissueDistributionDTO> tissueDistributionDTOS = new ArrayList<>();
        if (tissueDistributions != null){
            tissueDistributions.forEach(tissueDistribution -> tissueDistributionDTOS.add(toDto(tissueDistribution)));
        }
        return tissueDistributionDTOS;
    }

    @Override
    public TissueDistribution toEntity(TissueDistributionDTO dto)
    {
        return entityMapper.toTarget(dto, TissueDistribution.class);
    }

    @Override
    public Set<TissueDistribution> toEntities(Collection<TissueDistributionDTO> dtos)
    {
        Set<TissueDistribution> tissueDistributions = new HashSet<>();
        if (dtos != null)
        {
            dtos.forEach(tissueDistributionDTO -> tissueDistributions.add(toEntity(tissueDistributionDTO)));
        }
        return tissueDistributions;
    }
}
