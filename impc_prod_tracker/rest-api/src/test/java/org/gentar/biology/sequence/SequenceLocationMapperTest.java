package org.gentar.biology.sequence;

import org.gentar.biology.location.Location;
import org.gentar.biology.location.LocationDTO;
import org.gentar.biology.location.LocationMapper;
import org.gentar.biology.sequence_location.SequenceLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SequenceLocationMapperTest
{
    public static final long ID = 1L;
    public static final int INDEX = 1;
    private SequenceLocationMapper testInstance;

    @Mock
    private LocationMapper locationMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new SequenceLocationMapper(locationMapper);
    }

    @Test
    void toDto()
    {
        SequenceLocation sequenceLocation = new SequenceLocation();
        sequenceLocation.setId(ID);
        sequenceLocation.setIndex(INDEX);
        Sequence sequence = new Sequence();
        sequenceLocation.setSequence(sequence);
        Location location = new Location();
        sequenceLocation.setLocation(location);

        LocationDTO mockLocationDto = new LocationDTO();

        when(locationMapper.toDto(location)).thenReturn(mockLocationDto);

        SequenceLocationDTO sequenceLocationDTO = testInstance.toDto(sequenceLocation);

        assertThat(sequenceLocationDTO.getId(), is(ID));
        assertThat(sequenceLocationDTO.getLocationIndex(), is(INDEX));
        assertThat(sequenceLocationDTO.getLocationDTO(), is(mockLocationDto));
    }

    @Test
    void toEntity()
    {
        SequenceLocationDTO sequenceLocationDTO = new SequenceLocationDTO();
        sequenceLocationDTO.setId(null);
        sequenceLocationDTO.setLocationIndex(INDEX);
        LocationDTO locationDTO = new LocationDTO();
        sequenceLocationDTO.setLocationDTO(locationDTO);

        SequenceLocation sequenceLocation = testInstance.toEntity(sequenceLocationDTO);

        assertThat(sequenceLocation.getId(), is(nullValue()));
        assertThat(sequenceLocation.getIndex(), is(INDEX));
        verify(locationMapper, times(1)).toEntity(locationDTO);
    }
}
