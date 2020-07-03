package org.gentar.biology.mutation;

import org.gentar.audit.history.History;

public interface MutationService
{
    Mutation getById(Long id);
    Mutation getByIdFailsIfNull(Long id);
    Mutation getMutationByMinFailsIfNull(String min);
    Mutation create(Mutation mutation);
    History update(Mutation mutation);
}
