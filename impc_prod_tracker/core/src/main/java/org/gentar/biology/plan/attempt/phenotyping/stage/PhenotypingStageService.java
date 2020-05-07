package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.statemachine.TransitionEvaluation;
import java.util.List;

public interface PhenotypingStageService
{
    /**
     * Evaluates the transitions for a phenotyping stage  given its current status. To do that, this
     * method resolves the correct state machine for this plan and then checks what are the
     * possible transitions, evaluating each one and seeing if they could be executed
     * by the user or not.
     * @param phenotypingStage PhenotypingStage to evaluate.
     * @return The list of TransitionEvaluation that informs for each transition if it can
     * be executed or not, as long as a note explaining why in case it cannot be executed.
     */
    List<TransitionEvaluation> evaluateNextTransitions(PhenotypingStage phenotypingStage);
}
