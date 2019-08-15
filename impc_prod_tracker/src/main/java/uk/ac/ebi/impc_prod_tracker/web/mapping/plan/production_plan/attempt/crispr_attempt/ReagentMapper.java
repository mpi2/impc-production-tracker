package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan.attempt.crispr_attempt;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.crispr_attempt_reagent.CrisprAttemptReagent;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.ReagentDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
class ReagentMapper
{
    ReagentDTO toDto(CrisprAttemptReagent reagent)
    {
        ReagentDTO reagentDTO = new ReagentDTO();
        if (reagent.getReagent() != null)
        {
            reagentDTO.setName(reagent.getReagent().getName());
        }
        reagentDTO.setConcentration(reagent.getConcentration());
        return reagentDTO;
    }

    List<ReagentDTO> toDtos(Collection<CrisprAttemptReagent> crisprAttemptReagents)
    {
        List<ReagentDTO> reagentDTOs = new ArrayList<>();
        crisprAttemptReagents.forEach(x -> reagentDTOs.add(toDto(x)));
        return reagentDTOs;
    }
}
