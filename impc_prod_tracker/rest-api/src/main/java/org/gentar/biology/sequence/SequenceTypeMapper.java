package org.gentar.biology.sequence;

import org.gentar.Mapper;
import org.gentar.biology.sequence.type.SequenceType;
import org.gentar.biology.sequence.type.SequenceTypeService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class SequenceTypeMapper implements Mapper<SequenceType, String>
{
    private final SequenceTypeService sequenceTypeService;

    private static final String SEQUENCE_TYPE_NOT_FOUND_ERROR = "Sequence type '%s' does not exist.";

    public SequenceTypeMapper(SequenceTypeService sequenceTypeService) { this.sequenceTypeService = sequenceTypeService; }

    public SequenceType toEntity(String typeName)
    {
        SequenceType sequenceType = sequenceTypeService.getSequenceTypeByName(typeName);
        if (sequenceType == null)
        {
            throw new UserOperationFailedException(String.format(SEQUENCE_TYPE_NOT_FOUND_ERROR, typeName));
        }
        return sequenceType;
    }

    public String toDto(SequenceType entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }
}
