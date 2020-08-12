package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import java.util.List;

public interface MutationService
{
    Mutation getById(Long id);
    Mutation getMutationByMinFailsIfNull(String min);
    Mutation create(Mutation mutation);
    History update(Mutation mutation);
    /**
     * Gets the history for a mutation.
     * @param mutation The plan.
     * @return List of {@link History} with the trace of the changes for a mutation.
     */
    List<History> getHistory(Mutation mutation);
    String getSuggestedSymbol(Mutation mutation);
}
