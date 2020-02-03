package org.gentar.biology.specimen;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class SpecimenMapper implements Mapper<Specimen, SpecimenDTO>
{
    private EntityMapper entityMapper;
    private SpecimenService specimenService;

    public SpecimenMapper(EntityMapper entityMapper, SpecimenService specimenService)
    {
        this.entityMapper = entityMapper;
        this.specimenService = specimenService;
    }

    @Override
    public SpecimenDTO toDto(Specimen entity)
    {
        return entityMapper.toTarget(entity, SpecimenDTO.class);
    }

    @Override
    public Specimen toEntity(SpecimenDTO dto)
    {
        Specimen specimen = entityMapper.toTarget(dto, Specimen.class);
        specimen.setSpecimenType(specimenService.getSpecimenTypeByName(dto.getSpecimenTypeName()));
        return specimen;
    }
}
