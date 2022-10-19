package org.gentar.biology.gene.gene_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneSymbolMgiUpdateLogRepository
    extends JpaRepository<GeneSymbolMgiUpdateLog, Long> {
}
