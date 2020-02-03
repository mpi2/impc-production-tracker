package org.gentar.biology.specimen;

import org.gentar.EntityMapper;
import org.gentar.Mapper;

public class SpecimenPropertyMapper implements Mapper<SpecimenProperty, SpecimenPropertyDTO>
{
    private EntityMapper entityMapper;

    public SpecimenPropertyMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public SpecimenPropertyDTO toDto(SpecimenProperty entity)
    {
        return entityMapper.toTarget(entity, SpecimenPropertyDTO.class);
    }

    @Override
    public SpecimenProperty toEntity(SpecimenPropertyDTO dto)
    {
        return entityMapper.toTarget(dto, SpecimenProperty.class);
    }
}
