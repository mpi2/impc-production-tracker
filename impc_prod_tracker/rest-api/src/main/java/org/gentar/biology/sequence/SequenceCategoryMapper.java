package org.gentar.biology.sequence;

import org.gentar.Mapper;
import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.category.SequenceCategoryService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class SequenceCategoryMapper implements Mapper<SequenceCategory, String>
{
    private SequenceCategoryService sequenceCategoryService;

    private static final String SEQUENCE_CATEGORY_NOT_FOUND_ERROR = "Sequence category '%s' does not exist.";

    public SequenceCategoryMapper(SequenceCategoryService sequenceCategoryService) { this.sequenceCategoryService = sequenceCategoryService; }

    public SequenceCategory toEntity(String categoryName)
    {
        SequenceCategory sequenceCategory = sequenceCategoryService.getSequenceCategoryByName(categoryName);
        if (sequenceCategory == null)
        {
            throw new UserOperationFailedException(String.format(SEQUENCE_CATEGORY_NOT_FOUND_ERROR, categoryName));
        }
        return sequenceCategory;
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
