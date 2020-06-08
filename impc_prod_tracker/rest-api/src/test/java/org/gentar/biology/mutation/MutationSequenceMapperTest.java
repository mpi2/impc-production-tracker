package org.gentar.biology.mutation;

import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.SequenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MutationSequenceMapperTest
{
    private MutationSequenceMapper testInstance;

    @Mock
    private SequenceMapper sequenceMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new MutationSequenceMapper(sequenceMapper);
    }

    @Test
    void toDto()
    {
        MutationSequence mutationSequence = new MutationSequence();
        mutationSequence.setId(1L);
        mutationSequence.setIndex(1);

        MutationSequenceDTO mutationSequenceDTO = testInstance.toDto(mutationSequence);

        verify(sequenceMapper, times(1)).toDto(mutationSequence.getSequence());
        assertThat(mutationSequenceDTO.getId(), is(1L));
        assertThat(mutationSequenceDTO.getIndex(), is(1));
    }

    @Test
    void toEntity()
    {
        MutationSequenceDTO mutationSequenceDTO = new MutationSequenceDTO();
        mutationSequenceDTO.setId(1L);
        mutationSequenceDTO.setIndex(1);

        MutationSequence mutationSequence = testInstance.toEntity(mutationSequenceDTO);

        verify(sequenceMapper, times(1)).toEntity(mutationSequenceDTO.getSequenceDTO());
        assertThat(mutationSequence.getId(), is(1L));
        assertThat(mutationSequence.getIndex(), is(1));
    }
}