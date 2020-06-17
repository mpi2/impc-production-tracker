package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainMapper;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingAttemptCommonMapper implements Mapper<PhenotypingAttempt, PhenotypingAttemptCommonDTO>
{
    private EntityMapper entityMapper;
    private StrainMapper strainMapper;

    public PhenotypingAttemptCommonMapper(EntityMapper entityMapper, StrainMapper strainMapper)
    {
        this.entityMapper = entityMapper;
        this.strainMapper = strainMapper;
    }

    @Override
    public PhenotypingAttemptCommonDTO toDto(PhenotypingAttempt entity)
    {
        PhenotypingAttemptCommonDTO phenotypingAttemptCommonDTO = entityMapper.toTarget(entity,
                PhenotypingAttemptCommonDTO.class);
        return phenotypingAttemptCommonDTO;
    }

    @Override
    public PhenotypingAttempt toEntity(PhenotypingAttemptCommonDTO dto)
    {
        PhenotypingAttempt phenotypingAttempt = entityMapper.toTarget(dto, PhenotypingAttempt.class);
        setStrain(phenotypingAttempt, dto);
        return phenotypingAttempt;
    }


    private void setStrain(PhenotypingAttempt phenotypingAttempt, PhenotypingAttemptCommonDTO dto)
    {
        Strain strain = strainMapper.toEntity(dto.getStrainName());
        phenotypingAttempt.setStrain(strain);
    }
}
