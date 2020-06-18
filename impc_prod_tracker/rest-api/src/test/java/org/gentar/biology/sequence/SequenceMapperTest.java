package org.gentar.biology.sequence;

import org.gentar.biology.sequence.category.SequenceCategory;
import org.gentar.biology.sequence.type.SequenceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SequenceMapperTest
{
    public static final String SEQUENCE = "CGAGTGA";
    public static final String SEQUENCE_CATEGORY_NAME = "sequenceCategoryName";
    public static final String SEQUENCE_TYPE_NAME = "sequenceTypeName";
    private SequenceMapper testInstance;

    @Mock
    private SequenceLocationMapper sequenceLocationMapper;
    @Mock
    private SequenceCategoryMapper sequenceCategoryMapper;
    @Mock
    private SequenceTypeMapper sequenceTypeMapper;


    @BeforeEach
    void setUp()
    {
        testInstance =
            new SequenceMapper(sequenceLocationMapper, sequenceCategoryMapper, sequenceTypeMapper);
    }

    @Test
    void toDto()
    {
        Sequence sequence = new Sequence();
        sequence.setSequence(SEQUENCE);
        SequenceCategory sequenceCategory = new SequenceCategory();
        sequenceCategory.setName(SEQUENCE_CATEGORY_NAME);
        sequence.setSequenceCategory(sequenceCategory);
        SequenceType sequenceType = new SequenceType();
        sequenceType.setName(SEQUENCE_TYPE_NAME);
        sequence.setSequenceType(sequenceType);

        SequenceDTO sequenceDTO = testInstance.toDto(sequence);

        assertThat(sequenceDTO.getSequence(), is(SEQUENCE));
        assertThat(sequenceDTO.getSequenceCategoryName(), is(SEQUENCE_CATEGORY_NAME));
        assertThat(sequenceDTO.getSequenceTypeName(), is(SEQUENCE_TYPE_NAME));
        verify(sequenceLocationMapper, times(1)).toDtos(sequence.getSequenceLocations());
    }

    @Test
    void toEntity()
    {
        SequenceDTO sequenceDTO = new SequenceDTO();
        sequenceDTO.setSequence(SEQUENCE);
        sequenceDTO.setSequenceCategoryName(SEQUENCE_CATEGORY_NAME);
        sequenceDTO.setSequenceTypeName(SEQUENCE_TYPE_NAME);
        sequenceDTO.setSequenceLocationDTOS(new ArrayList<>());

        Sequence sequence = testInstance.toEntity(sequenceDTO);

        assertThat(sequence.getSequence(), is(SEQUENCE));
        verify(sequenceCategoryMapper, times(1)).toEntity(SEQUENCE_CATEGORY_NAME);
        verify(sequenceTypeMapper, times(1)).toEntity(SEQUENCE_TYPE_NAME);
        verify(sequenceLocationMapper, times(1)).toEntities(sequenceDTO.getSequenceLocationDTOS());
    }
}