package org.gentar.biology.specimen;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.specimen.property.SpecimenProperty;
import org.springframework.stereotype.Component;

@Component
public class SpecimenPropertyMapper implements Mapper<SpecimenProperty, SpecimenPropertyDTO>
{
    private final EntityMapper entityMapper;

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
