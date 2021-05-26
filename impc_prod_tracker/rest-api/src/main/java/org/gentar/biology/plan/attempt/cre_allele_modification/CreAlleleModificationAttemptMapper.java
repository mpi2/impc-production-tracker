package org.gentar.biology.plan.attempt.cre_allele_modification;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CreAlleleModificationAttemptMapper implements Mapper<CreAlleleModificationAttempt,
                                                                  CreAlleleModificationAttemptDTO> {
    private EntityMapper entityMapper;

    public CreAlleleModificationAttemptMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public CreAlleleModificationAttemptDTO toDto(CreAlleleModificationAttempt entity)
    {
        return entityMapper.toTarget(entity, CreAlleleModificationAttemptDTO.class);
    }

    @Override
    public CreAlleleModificationAttempt toEntity(CreAlleleModificationAttemptDTO dto)
    {
        return entityMapper.toTarget(dto, CreAlleleModificationAttempt.class);
    }
}
