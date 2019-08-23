package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.Attempt;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.AttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt.CrisprAttemptMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttemptMapper
{
    private CrisprAttemptMapper crisprAttemptMapper;
    private static final String CRISPR_ATTEMPT_TYPE = "Crispr";

    public AttemptMapper(CrisprAttemptMapper crisprAttemptMapper)
    {
        this.crisprAttemptMapper = crisprAttemptMapper;
    }

    public AttemptDTO toDto(Attempt attempt)
    {
        AttemptDTO attemptDTO = new AttemptDTO();
        if (isACrisprAttempt(attempt))
        {
            attemptDTO.setCrisprAttemptDTO(crisprAttemptMapper.toDto(attempt.getCrisprAttempt()));
        }
        return attemptDTO;
    }

    private boolean isACrisprAttempt(Attempt attempt)
    {
        boolean result;
        if (attempt.getAttemptType() != null)
        {
            result = CRISPR_ATTEMPT_TYPE.equals(attempt.getAttemptType().getName());
        }
        return true; //TODO: Change this to actually check the type of the attempt.
    }

    public List<AttemptDTO> toDtos(List<Attempt> attempts)
    {
        List<AttemptDTO> attemptDTOs = new ArrayList<>();
        if (attempts != null)
        {
            attempts.forEach(attempt -> attemptDTOs.add(toDto(attempt)));
        }
        return attemptDTOs;
    }
}
