package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.canonical_targeted_exon.InsertionCanonicalTargetedExon;
import org.springframework.stereotype.Component;

@Component
public class InsertionCanonicalTargetedExonMapper implements Mapper<InsertionCanonicalTargetedExon, InsertionCanonicalTargetedExonDTO> {

    private final EntityMapper entityMapper;

    public InsertionCanonicalTargetedExonMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }


    @Override
    public InsertionCanonicalTargetedExonDTO toDto(InsertionCanonicalTargetedExon entity) {
        return entityMapper.toTarget(entity, InsertionCanonicalTargetedExonDTO.class);
    }

    public InsertionCanonicalTargetedExon toEntity(InsertionCanonicalTargetedExonDTO insertionCanonicalTargetedExonDTO) {
        InsertionCanonicalTargetedExon insertionCanonicalTargetedExon = new InsertionCanonicalTargetedExon();
        insertionCanonicalTargetedExon.setId(insertionCanonicalTargetedExonDTO.getId());
        insertionCanonicalTargetedExon.setExonId(insertionCanonicalTargetedExonDTO.getExonId());
        insertionCanonicalTargetedExon.setTranscript(insertionCanonicalTargetedExonDTO.getTranscript());

        return insertionCanonicalTargetedExon;

    }
}
