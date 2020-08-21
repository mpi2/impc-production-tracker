package org.gentar.biology.sequence.category;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SequenceCategoryServiceImpl implements SequenceCategoryService
{
    private final SequenceCategoryRepository sequenceCategoryRepository;

    private final static String OUTCOME_SEQUENCE = "outcome sequence";

    public SequenceCategoryServiceImpl (SequenceCategoryRepository sequenceCategoryRepository)
    {
        this.sequenceCategoryRepository = sequenceCategoryRepository;
    }

    @Override
    @Cacheable("sequenceCategories")
    public SequenceCategory getSequenceCategoryByName(String name)
    {
        return sequenceCategoryRepository.findByNameIgnoreCase(name);
    }

    @Override
    public SequenceCategory getOutcomeSequenceCategory()
    {
        return getSequenceCategoryByName(OUTCOME_SEQUENCE);
    }
}
