package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.TargetedExonDTO;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.TargetedExon;
import org.springframework.stereotype.Component;

@Component
public class TargetedExonMapper implements Mapper<TargetedExon, TargetedExonDTO> {

    private final EntityMapper entityMapper;

    public TargetedExonMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }


    @Override
    public TargetedExonDTO toDto(TargetedExon entity) {
        return entityMapper.toTarget(entity, TargetedExonDTO.class);
    }

    public TargetedExon toEntity(TargetedExonDTO targetedExonDTO) {
        TargetedExon targetedExon = new TargetedExon();
        targetedExon.setId(targetedExonDTO.getId());
        targetedExon.setExonId(targetedExonDTO.getExonId());
        return targetedExon;

    }
}
