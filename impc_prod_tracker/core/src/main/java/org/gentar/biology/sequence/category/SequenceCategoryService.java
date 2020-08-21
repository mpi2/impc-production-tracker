package org.gentar.biology.sequence.category;

import org.springframework.cache.annotation.Cacheable;

public interface SequenceCategoryService
{
    SequenceCategory getSequenceCategoryByName(String name);
    SequenceCategory getOutcomeSequenceCategory();
}
