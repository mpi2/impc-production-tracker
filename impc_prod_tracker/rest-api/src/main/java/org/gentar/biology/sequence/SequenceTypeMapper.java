package org.gentar.biology.sequence;

import org.gentar.biology.sequence.type.SequenceType;
import org.gentar.biology.sequence.type.SequenceTypeService;
import org.springframework.stereotype.Component;

@Component
public class SequenceTypeMapper {
    private SequenceTypeService sequenceTypeService;

    public SequenceTypeMapper(SequenceTypeService sequenceTypeService) { this.sequenceTypeService = sequenceTypeService; }

    public SequenceType toEntity(String typeName)
    {
        return sequenceTypeService.getSequenceTypeByName(typeName);
    }
}
