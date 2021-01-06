package org.gentar.biology.outcome;

import org.gentar.biology.mutation.Mutation;
import org.springframework.beans.factory.annotation.Value;

public interface OutcomeMutationProjection {

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

    @Value("#{target.mutationId}")
    Long getMutationId();

    @Value("#{target.mutation}")
    Mutation getMutation();

}
