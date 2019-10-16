package uk.ac.ebi.impc_prod_tracker.data.biology.sequence_location;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence.Sequence;

import java.util.List;

public interface SequenceLocationRepository extends CrudRepository<SequenceLocation, Long> {
    List<SequenceLocation> findAllBySequence(Sequence sequence);
}
