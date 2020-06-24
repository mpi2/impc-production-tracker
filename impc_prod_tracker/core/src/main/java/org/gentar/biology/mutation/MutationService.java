package org.gentar.biology.mutation;

import org.gentar.audit.history.History;

public interface MutationService
{
    Mutation getMutationByMinFailsIfNull(String min);

    Mutation create(Mutation mutation);

    History update(Mutation mutation);
}
