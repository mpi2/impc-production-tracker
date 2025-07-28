package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.InsertionTargetedExon;
import org.springframework.stereotype.Component;

@Component
public class InsertionTargetedExonMapper implements Mapper<InsertionTargetedExon, InsertionTargetedExonDTO> {

    private final EntityMapper entityMapper;

    public InsertionTargetedExonMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }


    @Override
    public InsertionTargetedExonDTO toDto(InsertionTargetedExon entity) {
        return entityMapper.toTarget(entity, InsertionTargetedExonDTO.class);
    }

    public InsertionTargetedExon toEntity(InsertionTargetedExonDTO insertionTargetedExonDTO) {
        InsertionTargetedExon insertionTargetedExon = new InsertionTargetedExon();
        insertionTargetedExon.setId(insertionTargetedExonDTO.getId());
        insertionTargetedExon.setExonId(insertionTargetedExonDTO.getExonId());
        insertionTargetedExon.setTranscript(insertionTargetedExonDTO.getTranscript());

        return insertionTargetedExon;

    }
}
