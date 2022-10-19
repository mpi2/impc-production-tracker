package org.gentar.biology.targ_rep.gene.targ_rep_gene_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargRepGeneSymbolMgiUpdateLogRepository extends
    JpaRepository<TargRepGeneSymbolMgiUpdateLog, Long> {
}
