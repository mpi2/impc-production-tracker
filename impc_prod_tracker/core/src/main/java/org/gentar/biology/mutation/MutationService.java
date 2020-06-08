package org.gentar.biology.mutation;

public interface MutationService
{
    /**
     * Gets a mutation that belongs to a outcome (tpo) belonging to a plan (pin).
     * If the plan, outcome or id don't exist or there is not relation between them, an exception
     * will be thrown.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome
     * @param id Id of thr mutation.
     * @return The mutation object identified by id.
     */
    Mutation getMutationByPinTpoAndId(String pin, String tpo, Long id);
}
