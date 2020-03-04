package org.gentar.biology.sequence.category;

import org.springframework.data.repository.CrudRepository;

public interface SequenceCategoryRepository extends CrudRepository<SequenceCategory, Long>
{
    SequenceCategory findByNameIgnoreCase(String name);
}
