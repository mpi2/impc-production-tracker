package uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_donor;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;

public interface MutagenesisDonorRepository extends CrudRepository<MutagenesisDonor, Long> {
    Iterable<MutagenesisDonor> findAllByCrisprAttempt(CrisprAttempt crisprAttempt);
}
