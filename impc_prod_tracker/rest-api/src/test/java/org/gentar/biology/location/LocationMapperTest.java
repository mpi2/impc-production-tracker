package org.gentar.biology.location;

import org.gentar.biology.species.Species;
import org.gentar.biology.species.SpeciesMapper;
import org.gentar.biology.strain.Strain;
import org.gentar.biology.strain.StrainMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationMapperTest
{
    public static final long ID = 1L;
    public static final String CHR = "X";
    public static final long START = 100L;
    public static final long STOP = 200L;
    public static final String STRAND = "Strand";
    public static final String GENOME_BUILD = "GenomeBuild";
    public static final String STRAIN = "Strain";
    public static final String SPECIES = "Species";
    private LocationMapper testInstance;

    @Mock
    private StrainMapper strainMapper;
    @Mock
    private SpeciesMapper speciesMapper;

    @BeforeEach
    void setUp()
    {
        testInstance = new LocationMapper(strainMapper, speciesMapper);
    }

    @Test
    void toDto()
    {
        Location location = new Location();
        location.setId(ID);
        location.setChr(CHR);
        location.setStart(START);
        location.setStop(STOP);
        location.setStrand(STRAND);
        location.setGenomeBuild(GENOME_BUILD);
        Strain strain = new Strain();
        strain.setName(STRAIN);
        location.setStrain(strain);
        Species species = new Species();
        species.setName(SPECIES);
        location.setSpecies(species);

        LocationDTO locationDTO = testInstance.toDto(location);

        assertThat(locationDTO.getId(), is(ID));
        assertThat(locationDTO.getChr(), is(CHR));
        assertThat(locationDTO.getStart(), is(START));
        assertThat(locationDTO.getStop(), is(STOP));
        assertThat(locationDTO.getStrand(), is(STRAND));
        assertThat(locationDTO.getGenomeBuild(), is(GENOME_BUILD));
        assertThat(locationDTO.getStrainName(), is(STRAIN));
        assertThat(locationDTO.getSpeciesName(), is(SPECIES));
    }

    @Test
    void toEntity()
    {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setChr(CHR);
        locationDTO.setStart(START);
        locationDTO.setStop(STOP);
        locationDTO.setStrand(STRAND);
        locationDTO.setGenomeBuild(GENOME_BUILD);
        locationDTO.setStrainName(STRAIN);
        locationDTO.setSpeciesName(SPECIES);

        Strain mockStrain = new Strain();
        mockStrain.setName(STRAIN);
        Species mockSpecies = new Species();
        mockSpecies.setName(SPECIES);

        when(strainMapper.toEntity(STRAIN)).thenReturn(mockStrain);
        when(speciesMapper.toEntity(SPECIES)).thenReturn(mockSpecies);

        Location location = testInstance.toEntity(locationDTO);

        assertThat(location.getId(), is(nullValue()));
        assertThat(location.getChr(), is(CHR));
        assertThat(location.getStart(), is(START));
        assertThat(location.getStop(), is(STOP));
        assertThat(location.getStrand(), is(STRAND));
        assertThat(location.getGenomeBuild(), is(GENOME_BUILD));
        assertThat(location.getStrain().getName(), is(STRAIN));
        assertThat(location.getSpecies().getName(), is(SPECIES));
    }
}
