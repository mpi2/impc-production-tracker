package org.gentar.biology.plan.attempt.crispr;

import org.gentar.Mapper;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.attempt.crispr.assay.AssayType;

@Component
public class AssayMapper implements Mapper<Assay, AssayDTO>
{
    private final EntityMapper entityMapper;
    private final CrisprAttemptService crisprAttemptService;

    public AssayMapper(EntityMapper entityMapper, CrisprAttemptService crisprAttemptService)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptService = crisprAttemptService;
    }

    @Override
    public AssayDTO toDto(Assay entity) {
        AssayDTO assayDTO = entityMapper.toTarget(entity, AssayDTO.class);

        assayDTO.setId(entity.getId());
        assayDTO.setTypeName(entity.getAssayType().getName());
        assayDTO.setNumFounderPups(entity.getNumFounderPups());
        assayDTO.setNumFounderSelectedForBreeding(entity.getNumFounderSelectedForBreeding());
        assayDTO.setFounderNumAssays(entity.getFounderNumAssays());
        assayDTO.setNumDeletionG0Mutants(entity.getNumDeletionG0Mutants());
        assayDTO.setNumG0WhereMutationDetected(entity.getNumG0WhereMutationDetected());
        assayDTO.setNumHdrG0Mutants(entity.getNumHdrG0Mutants());
        assayDTO.setNumHdrG0MutantsAllDonorsInserted(entity.getNumHdrG0MutantsAllDonorsInserted());
        assayDTO.setNumHdrG0MutantsSubsetDonorsInserted(entity.getNumHdrG0MutantsSubsetDonorsInserted());
        assayDTO.setNumHrG0Mutants(entity.getNumHrG0Mutants());
        assayDTO.setNumNhejG0Mutants(entity.getNumNhejG0Mutants());

        return assayDTO;
    }

    public Assay toEntity(AssayDTO assayDTO)
    {
        Assay assay = entityMapper.toTarget(assayDTO, Assay.class);
        if (assay != null)
        {
            String assayTypeName = assayDTO.getTypeName();
            if (assayTypeName != null)
            {
                AssayType assayType = crisprAttemptService.getAssayTypeByName(assayTypeName);
                assay.setAssayType(assayType);
            }
        }
        return assay;
    }
}
