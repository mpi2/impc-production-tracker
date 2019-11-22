package org.gentar.biology.crispr_attempt.mutagenesis_donor;

import org.springframework.data.repository.CrudRepository;
import org.gentar.biology.crispr_attempt.CrisprAttempt;
import java.util.List;

public interface MutagenesisDonorRepository extends CrudRepository<MutagenesisDonor, Long> {
    List<MutagenesisDonor> findAllByCrisprAttempt(CrisprAttempt crisprAttempt);
}
