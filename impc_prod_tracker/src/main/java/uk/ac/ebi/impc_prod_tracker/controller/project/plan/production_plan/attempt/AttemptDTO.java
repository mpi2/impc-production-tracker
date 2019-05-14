package uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.crispr_attempt.CrisprAttemptDTO;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttemptDTO
{
    private String attemptType;
    private CrisprAttemptDTO crisprAttempt;
}
