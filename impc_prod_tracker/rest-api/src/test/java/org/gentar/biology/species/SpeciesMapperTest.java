package org.gentar.biology.species;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpeciesMapperTest
{
    public static final String SPECIES_NAME = "speciesName";
    private SpeciesMapper testInstance;

    @Mock
    private SpeciesService speciesService;

    @BeforeEach
    void setUp()
    {
        testInstance = new SpeciesMapper(speciesService);
    }

    @Test
    void toDto()
    {
        Species species = new Species();
        species.setName(SPECIES_NAME);

        String speciesName = testInstance.toDto(species);

        assertThat(speciesName, is(SPECIES_NAME));
    }

    @Test
    void toEntity()
    {
        String speciesName = SPECIES_NAME;
        Species expectedResult = new Species();
        expectedResult.setName(SPECIES_NAME);
        when(speciesService.getSpeciesByName(speciesName)).thenReturn(expectedResult);

        Species result = testInstance.toEntity(speciesName);

        verify(speciesService, times(1)).getSpeciesByName(speciesName);
        assertThat(result.getName(), is(SPECIES_NAME));
    }
}
