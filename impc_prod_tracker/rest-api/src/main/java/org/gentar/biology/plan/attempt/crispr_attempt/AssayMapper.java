package org.gentar.biology.plan.attempt.crispr_attempt;

import org.gentar.biology.plan.attempt.crispr.CrisprAttemptService;
import org.gentar.biology.plan.production.crispr_attempt.AssayDTO;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.attempt.crispr.assay.AssayType;

@Component
public class AssayMapper
{
    private EntityMapper entityMapper;
    private CrisprAttemptService crisprAttemptService;

    public AssayMapper(EntityMapper entityMapper, CrisprAttemptService crisprAttemptService)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptService = crisprAttemptService;
    }

    public Assay toEntity(AssayDTO assayDTO)
    {
        Assay assay = entityMapper.toTarget(assayDTO, Assay.class);
        String assayTypeName = assayDTO.getTypeName();
        if (assayTypeName != null)
        {
            AssayType assayType = crisprAttemptService.getAssayTypeByName(assayTypeName);
            assay.setAssayType(assayType);
        }

        return assay;
    }
}
