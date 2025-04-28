package org.gentar.biology.mutation;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.CanonicalTargetedExonDTO;
import org.gentar.biology.plan.attempt.crispr.canonical_targeted_exon.targeted_exon.CanonicalTargetedExon;
import org.springframework.stereotype.Component;

@Component
public class CanonicalTargetedExonMapper implements Mapper<CanonicalTargetedExon, CanonicalTargetedExonDTO> {

    private final EntityMapper entityMapper;

    public CanonicalTargetedExonMapper(EntityMapper entityMapper) {
        this.entityMapper = entityMapper;
    }


    @Override
    public CanonicalTargetedExonDTO toDto(CanonicalTargetedExon entity) {
        return entityMapper.toTarget(entity, CanonicalTargetedExonDTO.class);
    }

    public CanonicalTargetedExon toEntity(CanonicalTargetedExonDTO canonicalTargetedExonDTO) {
        CanonicalTargetedExon canonicalTargetedExon = new CanonicalTargetedExon();
        canonicalTargetedExon.setId(canonicalTargetedExonDTO.getId());
        canonicalTargetedExon.setExonId(canonicalTargetedExonDTO.getExonId());
        return canonicalTargetedExon;

    }
}
