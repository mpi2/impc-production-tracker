package org.gentar.biology.plan.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr_attempt.StrainMapper;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingAttemptMapper implements Mapper<PhenotypingAttempt, PhenotypingAttemptDTO>
{
    private EntityMapper entityMapper;
    private TissueDistributionMapper tissueDistributionMapper;
    private StrainMapper strainMapper;

    public PhenotypingAttemptMapper(
        EntityMapper entityMapper,
        TissueDistributionMapper tissueDistributionMapper,
        StrainMapper strainMapper)
    {
        this.entityMapper = entityMapper;
        this.tissueDistributionMapper = tissueDistributionMapper;
        this.strainMapper = strainMapper;
    }

    @Override
    public PhenotypingAttemptDTO toDto(PhenotypingAttempt entity)
    {
        PhenotypingAttemptDTO phenotypingAttemptDTO =
            entityMapper.toTarget(entity, PhenotypingAttemptDTO.class);
        phenotypingAttemptDTO.setTissueDistributionCentreDTOs(
            tissueDistributionMapper.toDtos(entity.getTissueDistributions()));
        phenotypingAttemptDTO.setStrainDTO(strainMapper.toDto(entity.getStrain()));
        return phenotypingAttemptDTO;
    }

    @Override
    public PhenotypingAttempt toEntity(PhenotypingAttemptDTO dto)
    {
        return entityMapper.toTarget(dto, PhenotypingAttempt.class);
    }
}
