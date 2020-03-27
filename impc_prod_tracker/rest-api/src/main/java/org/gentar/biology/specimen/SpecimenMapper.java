package org.gentar.biology.specimen;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.StrainMapper;
import org.springframework.stereotype.Component;

@Component
public class SpecimenMapper implements Mapper<Specimen, SpecimenDTO>
{
    private EntityMapper entityMapper;
    private SpecimenService specimenService;
    private StrainMapper strainMapper;

    public SpecimenMapper(EntityMapper entityMapper, SpecimenService specimenService, StrainMapper strainMapper)
    {
        this.entityMapper = entityMapper;
        this.specimenService = specimenService;
        this.strainMapper = strainMapper;
    }

    @Override
    public SpecimenDTO toDto(Specimen entity)
    {
        SpecimenDTO specimenDTO = entityMapper.toTarget(entity, SpecimenDTO.class);
        specimenDTO.setStrainName(strainMapper.toDto(entity.getStrain()));
        return specimenDTO;
    }

    @Override
    public Specimen toEntity(SpecimenDTO dto)
    {
        Specimen specimen = entityMapper.toTarget(dto, Specimen.class);
        specimen.setSpecimenType(specimenService.getSpecimenTypeByName(dto.getSpecimenTypeName()));
        specimen.setStrain(strainMapper.toEntity(dto.getStrainName()));
        return specimen;
    }
}
