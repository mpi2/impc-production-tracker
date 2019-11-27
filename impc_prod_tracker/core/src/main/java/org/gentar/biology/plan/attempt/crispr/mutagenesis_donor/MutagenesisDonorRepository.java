package org.gentar.biology.plan.attempt.crispr.mutagenesis_donor;

import org.springframework.data.repository.CrudRepository;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import java.util.List;

public interface MutagenesisDonorRepository extends CrudRepository<MutagenesisDonor, Long> {
    List<MutagenesisDonor> findAllByCrisprAttempt(CrisprAttempt crisprAttempt);
}
