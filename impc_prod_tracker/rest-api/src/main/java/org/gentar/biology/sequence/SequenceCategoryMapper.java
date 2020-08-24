package org.gentar.biology.sequence;

import org.gentar.Mapper;
import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.category.SequenceCategoryService;
import org.springframework.stereotype.Component;

@Component
public class SequenceCategoryMapper implements Mapper<SequenceCategory, String>
{
    private final SequenceCategoryService sequenceCategoryService;

    public SequenceCategoryMapper(SequenceCategoryService sequenceCategoryService)
    {
        this.sequenceCategoryService = sequenceCategoryService;
    }

    public SequenceCategory toEntity(String categoryName)
    {
        return sequenceCategoryService.getSequenceCategoryByName(categoryName);
    }

    public String toDto(SequenceCategory entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }
}
