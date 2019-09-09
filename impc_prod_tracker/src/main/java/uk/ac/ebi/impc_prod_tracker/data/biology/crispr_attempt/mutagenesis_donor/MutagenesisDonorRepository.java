package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_donor;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import java.util.List;

public interface MutagenesisDonorRepository extends CrudRepository<MutagenesisDonor, Long> {
    List<MutagenesisDonor> findAllByCrisprAttempt(CrisprAttempt crisprAttempt);
}
