package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    /**
     * Gets the Process event that corresponds to the name of the transition 'name'.
     * @param phenotypingStage phenotypingStage to evaluate.
     * @param name The name of the transition that is going to be evaluated on the phenotypingStage.
     * @return A {@link ProcessEvent} corresponding to the given 'name'.
     */
    ProcessEvent getProcessEventByName(PhenotypingStage phenotypingStage, String name);

    /**
     * Get a phenotyping stage given its pps id.
     * @param psn Unique id of the phenotyping stage
     * @return phenotyping stage
     */
    PhenotypingStage getByPsnFailsIfNotFound(String psn);

    /**
     * Get an phenotyping stage identified by 'pps' and belonging to plan 'pin'.
     * @param pin Public id of the plan.
     * @param psn Public id of the phenotyping stage.
     * @return Found phenotyping stage.
     * @throws UserOperationFailedException if the plan or phenotyping stage don't exist or if
     * they exist but they are not related.
     */
    PhenotypingStage getPhenotypingStageByPinAndPsn(String pin, String psn);

    /**
     * Find all the phenotyping stages.
     * @return A list of {@link PhenotypingStage}
     */
    List<PhenotypingStage> getPhenotypingStages();

    /**
     * Creates a phenotyping stage in the system.
     * @param phenotypingStage In memory phenotyping stage object. Without ids. Not saved in database yet.
     * @return The phenotyping stage saved in the database.
     */
    PhenotypingStage create(PhenotypingStage phenotypingStage);

    /**
     * Updates an existing phenotyping stage.
     * @param phenotypingStage New data for the phenotyping stage.
     * @return
     */
    History update(PhenotypingStage phenotypingStage);

    /**
     * Gets the history for a phenotyping stage.
     * @param phenotypingStage The phenotyping stage.
     * @return List of {@link History} with the trace of the changes for a phenotyping stage.
     */
    List<History> getPhenotypingStageHistory(PhenotypingStage phenotypingStage);

    /**
     * Gets a {@link PhenotypingStageType} object based in its name.
     * @param name Name of the {@link PhenotypingStageType} object.
     * @return {@link PhenotypingStageType} object.
     */
    PhenotypingStageType getPhenotypingStageTypeByNameFailingWhenNull(String name);

    /**
     * Gets a {@link PhenotypingStageType} object based in its name.
     * @param name Name of the {@link PhenotypingStageType} object.
     * @return {@link PhenotypingStageType} object.
     */
    PhenotypingStageType getPhenotypingStageTypeByName(String name);
}
