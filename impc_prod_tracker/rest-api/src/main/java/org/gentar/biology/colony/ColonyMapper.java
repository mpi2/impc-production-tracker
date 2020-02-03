package org.gentar.biology.colony;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class ColonyMapper implements Mapper<Colony, ColonyDTO>
{
    private EntityMapper entityMapper;

    public ColonyMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public ColonyDTO toDto(Colony entity)
    {
        return entityMapper.toTarget(entity, ColonyDTO.class);
    }

    @Override
    public Colony toEntity(ColonyDTO dto)
    {
        return entityMapper.toTarget(dto, Colony.class);
    }
}
