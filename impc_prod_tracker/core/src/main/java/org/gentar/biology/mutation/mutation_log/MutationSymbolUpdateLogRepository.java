package org.gentar.biology.mutation.mutation_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MutationSymbolUpdateLogRepository
    extends JpaRepository<MutationSymbolUpdateLog, Long> {
}
