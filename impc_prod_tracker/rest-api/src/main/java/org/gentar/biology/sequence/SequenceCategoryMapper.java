package org.gentar.biology.sequence;

import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.category.SequenceCategoryService;
import org.springframework.stereotype.Component;

@Component
public class SequenceCategoryMapper
{
    private SequenceCategoryService sequenceCategoryService;

    public SequenceCategoryMapper(SequenceCategoryService sequenceCategoryService) { this.sequenceCategoryService = sequenceCategoryService; }

    public SequenceCategory toEntity(String categoryName)
    {
        return sequenceCategoryService.getSequenceCategoryByName(categoryName);
    }
}
