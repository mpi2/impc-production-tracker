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
        if (assayDTO == null)
        {
            return null;
        }
        
        Assay assay = new Assay();
        
        // Only set ID if it exists (for updates), otherwise leave null for new entities
        if (assayDTO.getId() != null && assayDTO.getId() > 0)
        {
            assay.setId(assayDTO.getId());
        }
        
        assay.setNumFounderPups(assayDTO.getNumFounderPups());
        assay.setNumFounderSelectedForBreeding(assayDTO.getNumFounderSelectedForBreeding());
        assay.setFounderNumAssays(assayDTO.getFounderNumAssays());
        assay.setNumDeletionG0Mutants(assayDTO.getNumDeletionG0Mutants());
        assay.setNumG0WhereMutationDetected(assayDTO.getNumG0WhereMutationDetected());
        assay.setNumHdrG0Mutants(assayDTO.getNumHdrG0Mutants());
        assay.setNumHdrG0MutantsAllDonorsInserted(assayDTO.getNumHdrG0MutantsAllDonorsInserted());
        assay.setNumHdrG0MutantsSubsetDonorsInserted(assayDTO.getNumHdrG0MutantsSubsetDonorsInserted());
        assay.setNumHrG0Mutants(assayDTO.getNumHrG0Mutants());
        assay.setNumNhejG0Mutants(assayDTO.getNumNhejG0Mutants());
        
        String assayTypeName = assayDTO.getTypeName();
        if (assayTypeName != null)
        {
            AssayType assayType = crisprAttemptService.getAssayTypeByName(assayTypeName);
            assay.setAssayType(assayType);
        }
        
        return assay;
    }
}
