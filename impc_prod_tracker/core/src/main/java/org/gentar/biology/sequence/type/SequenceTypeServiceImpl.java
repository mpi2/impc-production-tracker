package org.gentar.biology.sequence.type;

import org.springframework.stereotype.Component;

@Component
public class SequenceTypeServiceImpl implements SequenceTypeService
{
    private final SequenceTypeRepository sequenceTypeRepository;

    public SequenceTypeServiceImpl(SequenceTypeRepository sequenceTypeRepository) { this.sequenceTypeRepository = sequenceTypeRepository; }

    public SequenceType getSequenceTypeByName (String typeName)
    {
        return sequenceTypeRepository.findByNameIgnoreCase(typeName);
    }
}
