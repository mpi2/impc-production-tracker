package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.springframework.stereotype.Component;

@Component
public class MutationQCResultMapper implements Mapper<MutationQcResult, MutationQCResultDTO>
{
    private EntityMapper entityMapper;

    public MutationQCResultMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    @Override
    public MutationQCResultDTO toDto(MutationQcResult entity)
    {
        return entityMapper.toTarget(entity, MutationQCResultDTO.class);
    }

    @Override
    public MutationQcResult toEntity(MutationQCResultDTO dto)
    {
        return entityMapper.toTarget(dto, MutationQcResult.class);
    }
}
