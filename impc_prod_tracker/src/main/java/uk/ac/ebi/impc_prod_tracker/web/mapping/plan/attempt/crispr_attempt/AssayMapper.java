package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.assay.Assay;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.assay.assay_type.AssayType;
import uk.ac.ebi.impc_prod_tracker.service.biology.plan.crispr.CrisprAttemptService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt.AssayDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.EntityMapper;

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
