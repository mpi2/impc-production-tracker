package org.gentar.biology.sequence.category;

import org.springframework.stereotype.Component;

@Component
public class SequenceCategoryServiceImpl implements SequenceCategoryService
{
    private SequenceCategoryRepository sequenceCategoryRepository;

    public SequenceCategoryServiceImpl (SequenceCategoryRepository sequenceCategoryRepository)
    {
        this.sequenceCategoryRepository = sequenceCategoryRepository;
    }

    public SequenceCategory getSequenceCategoryByName(String name) { return sequenceCategoryRepository.findByNameIgnoreCase(name); };
}
