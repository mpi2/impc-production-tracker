package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.project.Project;
import org.gentar.exceptions.UserOperationFailedException;

import java.util.List;

/**
 * The methods to control the information of outcomes.
 */
public interface OutcomeService
{
    /**
     * Get an outcome identified by 'tpo' and belonging to plan 'pin'.
     * @param pin Public id of the plan.
     * @param tpo Public id of the outcome.
     * @return Found outcome.
     * @throws UserOperationFailedException if the plan, outcome or mutation don't exist or if
     * they exist but they are not related.
     */
    Outcome getOutcomeByPinAndTpo(String pin, String tpo);

    /**
     * Find all the outcomes.
     * @return A list of {@link Outcome}
     */
    List<Outcome> getOutcomes();

    /**
     * Creates an outcome in the system.
     * @param outcome In memory outcome object. Without ids. Not saved in database yet.
     * @return The outcome saved in the database.
     */
    Outcome create(Outcome outcome);

    /**
     * Updates an existing outcome.
     * @param outcome New data for the outcome.
     * @return
     */
    History update(Outcome outcome);

    /**
     * Gets a {@link OutcomeType} object based in its name.
     * @param name Name of the {@link OutcomeType} object.
     * @return {@link OutcomeType} object.
     */
    OutcomeType getOutcomeTypeByName(String name);

    /**
     * Gets a {@link OutcomeType} object based in its name.
     * @param name Name of the {@link OutcomeType} object.
     * @return {@link OutcomeType} object.
     */
    OutcomeType getOutcomeTypeByNameFailingWhenNull(String name);

    /**
    * Gets an outcome given its tpo. Throws an exception if the tpo is not found.
    * @param tpo TPO
    * @return The Outcome
     */
    Outcome getByTpoFailsIfNotFound(String tpo);

    /**
     * Gets a mutation that belongs to a outcome (tpo) belonging to a plan (pin).
     * If the plan, outcome or id don't exist or there is not relation between them, an exception
     * will be thrown.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome
     * @param min Public identifier of the mutation.
     * @return The mutation object identified by id.
     */
    Mutation getMutationByPinTpoAndMin(String pin, String tpo, String min);

    /**
     * Creates the associations with mutations.
     * @param pin Public identifier for a plan.
     * @param tpo Public identifier for an outcome.
     * @param mins List of public mutation ids (
     */
    History createMutationsAssociations(String pin, String tpo, List<String> mins);

    /**
     * Deletes the associations with mutations.
     * @param pin Public identifier for a plan.
     * @param tpo Public identifier for an outcome.
     * @param mins List of public mutation ids. It does not delete the mutation itself, only the
     *             relation with the outcome.
     */
    History deleteMutationsAssociations(String pin, String tpo, List<String> mins);

    /**
     * Gets the history for a outcome
     * @param outcome The outcome.
     * @return List of {@link History} with the trace of the changes for a outcome.
     */
    List<History> getOutcomeHistory(Outcome outcome);

    Boolean associateOutcomeToPlan(Outcome outcome, String pin);
}
