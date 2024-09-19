package org.gentar.biology.mutation.mutation_ensembl;

import org.springframework.beans.factory.annotation.Value;


public interface mutationEnsemblMutationPartProjection {

    @Value("#{target.min}")
    String getMin();

    @Value("#{target.symbol}")
    String getSymbol();

    @Value("#{target.accId}")
    String getAccId();

}
