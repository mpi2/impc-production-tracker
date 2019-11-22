package org.gentar.web.mapping.plan.attempt.crispr_attempt;

import org.gentar.service.biology.plan.crispr.CrisprAttemptService;
import org.gentar.web.dto.plan.production.crispr_attempt.AssayDTO;
import org.gentar.web.mapping.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.crispr_attempt.assay.Assay;
import org.gentar.biology.crispr_attempt.assay.assay_type.AssayType;

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
