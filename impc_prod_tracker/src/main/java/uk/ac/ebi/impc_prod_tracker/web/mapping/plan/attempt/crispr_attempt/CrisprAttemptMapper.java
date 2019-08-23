package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt.CrisprAttemptDTO;

@Component
public class CrisprAttemptMapper
{
    private ModelMapper modelMapper;

    public CrisprAttemptMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public CrisprAttemptDTO toDto (CrisprAttempt crisprAttempt)
    {
        CrisprAttemptDTO crisprAttemptDTO = modelMapper.map(crisprAttempt, CrisprAttemptDTO.class);
        return crisprAttemptDTO;
    }
}
