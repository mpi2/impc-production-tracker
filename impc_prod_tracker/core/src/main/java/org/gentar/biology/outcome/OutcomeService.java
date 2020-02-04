package org.gentar.biology.outcome;

import java.util.List;

/**
 * The methods to control the information of outcomes.
 */
public interface OutcomeService
{
    /**
     * Find all the outcomes.
     * @return A list of {@link Outcome}
     */
    List<Outcome> findAll();

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
    Outcome update(Outcome outcome);

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
}
