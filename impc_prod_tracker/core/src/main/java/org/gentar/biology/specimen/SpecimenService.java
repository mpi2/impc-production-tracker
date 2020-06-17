package org.gentar.biology.specimen;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.specimen.type.SpecimenType;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import java.util.List;

public interface SpecimenService
{
    SpecimenType getSpecimenTypeByName(String name);

    /**
     * Gets the Process event that corresponds to the name of the transition 'name'.
     * @param specimen specimen to evaluate.
     * @param name The name of the transition that is going to be evaluated on the specimen.
     * @return A {@link ProcessEvent} corresponding to the given 'name'.
     */
    ProcessEvent getProcessEventByName(Specimen specimen, String name);

    /**
     * Evaluates the transitions for a colony given its current status. To do that, this
     * method resolves the correct state machine for this plan and then checks what are the
     * possible transitions, evaluating each one and seeing if they could be executed
     * by the user or not.
     * @param colony Colony to evaluate.
     * @return The list of TransitionEvaluation that informs for each transition if it can
     * be executed or not, as long as a note explaining why in case it cannot be executed.
     */
    List<TransitionEvaluation> evaluateNextTransitions(Specimen specimen);
}
