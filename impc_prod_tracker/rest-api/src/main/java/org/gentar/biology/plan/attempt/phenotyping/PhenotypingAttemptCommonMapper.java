package org.gentar.biology.plan.attempt.phenotyping;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainMapper;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingAttemptCommonMapper implements Mapper<PhenotypingAttempt, PhenotypingAttemptCommonDTO>
{
    private EntityMapper entityMapper;
    private StrainMapper strainMapper;
    private WorkUnitMapper workUnitMapper;

    public PhenotypingAttemptCommonMapper(EntityMapper entityMapper,
                                          StrainMapper strainMapper,
                                          WorkUnitMapper workUnitMapper)
    {
        this.entityMapper = entityMapper;
        this.strainMapper = strainMapper;
        this.workUnitMapper = workUnitMapper;
    }

    @Override
    public PhenotypingAttemptCommonDTO toDto(PhenotypingAttempt entity)
    {
        PhenotypingAttemptCommonDTO phenotypingAttemptCommonDTO = entityMapper.toTarget(entity,
                PhenotypingAttemptCommonDTO.class);

        if (phenotypingAttemptCommonDTO.getCohortWorkUnitName() == null)
        {
            phenotypingAttemptCommonDTO.setCohortWorkUnitName(entity.getPlan().getWorkUnit().getName());
        }
        return phenotypingAttemptCommonDTO;
    }

    @Override
    public PhenotypingAttempt toEntity(PhenotypingAttemptCommonDTO dto)
    {
        PhenotypingAttempt phenotypingAttempt = entityMapper.toTarget(dto, PhenotypingAttempt.class);
        setStrain(phenotypingAttempt, dto);
        setCohortWorkUnit(phenotypingAttempt, dto);
        return phenotypingAttempt;
    }


    private void setStrain(PhenotypingAttempt phenotypingAttempt, PhenotypingAttemptCommonDTO dto)
    {
        Strain strain = strainMapper.toEntity(dto.getStrainName());
        phenotypingAttempt.setStrain(strain);
    }

    private void setCohortWorkUnit(PhenotypingAttempt phenotypingAttempt, PhenotypingAttemptCommonDTO dto)
    {
        if (dto.getCohortWorkUnitName() != null)
        {
            WorkUnit workUnit = workUnitMapper.toEntity(dto.getCohortWorkUnitName());
            phenotypingAttempt.setCohortWorkUnit(workUnit);
        }
    }
}
