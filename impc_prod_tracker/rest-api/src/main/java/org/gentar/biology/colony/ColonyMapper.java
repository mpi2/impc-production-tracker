package org.gentar.biology.colony;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr_attempt.StrainMapper;
import org.gentar.biology.status.StatusService;
import org.springframework.stereotype.Component;

@Component
public class ColonyMapper implements Mapper<Colony, ColonyDTO>
{
    private EntityMapper entityMapper;
    private StatusService statusService;
    private StrainMapper strainMapper;

    public ColonyMapper(
        EntityMapper entityMapper, StatusService statusService, StrainMapper strainMapper)
    {
        this.entityMapper = entityMapper;
        this.statusService = statusService;
        this.strainMapper = strainMapper;
    }

    @Override
    public ColonyDTO toDto(Colony entity)
    {
        return entityMapper.toTarget(entity, ColonyDTO.class);
    }

    @Override
    public Colony toEntity(ColonyDTO dto)
    {
        Colony colony = entityMapper.toTarget(dto, Colony.class);
        colony.setStrain(strainMapper.toEntity(dto.getStrainDTO()));
        colony.setStatus(statusService.getStatusByName(dto.getStatusName()));
        return colony;
    }
}
