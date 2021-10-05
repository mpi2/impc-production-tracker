package org.gentar.biology.plan.attempt.es_cell_allele_modification;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainService;
import org.springframework.stereotype.Component;

@Component
public class EsCellAlleleModificationAttemptMapper implements Mapper<EsCellAlleleModificationAttempt,
        EsCellAlleleModificationAttemptDTO>
{
    private EntityMapper entityMapper;
    private StrainService strainService;

    public EsCellAlleleModificationAttemptMapper(EntityMapper entityMapper, StrainService strainService)
    {
        this.entityMapper = entityMapper;
        this.strainService = strainService;
    }

    @Override
    public EsCellAlleleModificationAttemptDTO toDto(EsCellAlleleModificationAttempt entity)
    {
        return entityMapper.toTarget(entity, EsCellAlleleModificationAttemptDTO.class);
    }

    @Override
    public EsCellAlleleModificationAttempt toEntity(EsCellAlleleModificationAttemptDTO dto)
    {
        EsCellAlleleModificationAttempt esCellAlleleModificationAttempt =
                entityMapper.toTarget(dto, EsCellAlleleModificationAttempt.class);
        Strain deleterStrain =  strainService.getStrainByName(dto.getDeleterStrainName());
        esCellAlleleModificationAttempt.setDeleterStrain(deleterStrain);
        return esCellAlleleModificationAttempt;
    }
}
