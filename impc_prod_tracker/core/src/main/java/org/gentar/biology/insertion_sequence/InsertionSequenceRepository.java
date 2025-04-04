package org.gentar.biology.insertion_sequence;

import org.springframework.data.repository.CrudRepository;

public interface InsertionSequenceRepository extends CrudRepository<InsertionSequence, Long>
{
    InsertionSequence findFirstById(Long id);
}
