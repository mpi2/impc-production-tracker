package org.gentar.biology.plan.attempt.phenotyping;

import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

@Primary
public interface PhenotypingAttemptRepository extends CrudRepository<PhenotypingAttempt, Long> {
}
