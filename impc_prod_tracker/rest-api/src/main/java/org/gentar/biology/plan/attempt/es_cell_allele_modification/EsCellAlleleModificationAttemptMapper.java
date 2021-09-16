package org.gentar.biology.plan.attempt.es_cell_allele_modification;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class EsCellAlleleModificationAttemptMapper implements Mapper<EsCellAlleleModificationAttempt,
        EsCellAlleleModificationAttemptDTO> {
    private EntityMapper entityMapper;

    public EsCellAlleleModificationAttemptMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public EsCellAlleleModificationAttemptDTO toDto(EsCellAlleleModificationAttempt entity)
    {
        return entityMapper.toTarget(entity, EsCellAlleleModificationAttemptDTO.class);
    }

    @Override
    public EsCellAlleleModificationAttempt toEntity(EsCellAlleleModificationAttemptDTO dto)
    {
        return entityMapper.toTarget(dto, EsCellAlleleModificationAttempt.class);
    }
}
