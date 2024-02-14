package org.gentar.biology.plan.attempt.crispr_allele_modification;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainService;
import org.springframework.stereotype.Component;

@Component
public class CrisprAlleleModificationAttemptMapper implements Mapper<CrisprAlleleModificationAttempt,
    CrisprAlleleModificationAttemptDTO>
{
    private final EntityMapper entityMapper;
    private final StrainService strainService;

    public CrisprAlleleModificationAttemptMapper(EntityMapper entityMapper, StrainService strainService)
    {
        this.entityMapper = entityMapper;
        this.strainService = strainService;
    }

    @Override
    public CrisprAlleleModificationAttemptDTO toDto(CrisprAlleleModificationAttempt entity)
    {
        return entityMapper.toTarget(entity, CrisprAlleleModificationAttemptDTO.class);
    }

    @Override
    public CrisprAlleleModificationAttempt toEntity(CrisprAlleleModificationAttemptDTO dto)
    {
        CrisprAlleleModificationAttempt crisprAlleleModificationAttempt =
                entityMapper.toTarget(dto, CrisprAlleleModificationAttempt.class);
        Strain deleterStrain =  strainService.getStrainByName(dto.getDeleterStrainName());
        crisprAlleleModificationAttempt.setDeleterStrain(deleterStrain);
        return crisprAlleleModificationAttempt;
    }
}
