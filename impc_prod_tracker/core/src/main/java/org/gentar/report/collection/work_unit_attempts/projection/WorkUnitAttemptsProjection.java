package org.gentar.report.collection.work_unit_attempts.projection;

import org.springframework.beans.factory.annotation.Value;

public interface WorkUnitAttemptsProjection {

    @Value("#{target.date}")
    String getDate();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.min}")
    String getMin();

    @Value("#{target.mutation_symbol}")
    String getMutationSymbol();

    @Value("#{target.gene_symbol}")
    String getGeneSymbol();
}
