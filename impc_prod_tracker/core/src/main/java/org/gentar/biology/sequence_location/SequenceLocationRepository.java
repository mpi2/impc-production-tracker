package org.gentar.biology.sequence_location;

import org.springframework.data.repository.CrudRepository;
import org.gentar.biology.sequence.Sequence;

import java.util.List;

public interface SequenceLocationRepository extends CrudRepository<SequenceLocation, Long>
{
    SequenceLocation findFirstById(Long id);
    List<SequenceLocation> findAllBySequence(Sequence sequence);
}
