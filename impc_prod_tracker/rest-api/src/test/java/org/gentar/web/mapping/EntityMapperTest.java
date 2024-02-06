package org.gentar.web.mapping;

import org.gentar.EntityMapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneCommonDTO;
import org.gentar.biology.species.Species;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest
@ActiveProfiles("test")
public class EntityMapperTest
{
    @Autowired
    private ModelMapper modelMapper;

    private static final Long ID1 = 1L;
    private static final Long ID2 = 2L;
    private static final String ID_LIST_VALUE = "idListValue1";
    private static final String SYMBOL = "symbol";
    private static final String SPECIES_NAME = "speciesName";

    @Test
    @Disabled
    public void testToDto()
    {
        EntityMapper entityMapper = new EntityMapper(modelMapper);
        Gene gene = buildGene(ID1);

        GeneCommonDTO geneDTO = entityMapper.toTarget(gene, GeneCommonDTO.class);

        assertThat("Gene Symbol", geneDTO.getSymbol(), is(SYMBOL + ID1));
        assertThat("Gene idListValue", geneDTO.getAccId(), is(ID_LIST_VALUE));
        assertThat("Gene Species Name", geneDTO.getSpeciesName(), is(SPECIES_NAME));
    }

    @Test
    @Disabled
    public void testToDtoNull()
    {
        EntityMapper entityMapper = new EntityMapper(modelMapper);

        GeneCommonDTO geneDTO = entityMapper.toTarget(null, GeneCommonDTO.class);

        assertThat("geneDTO", geneDTO, is(nullValue()));
    }

    @Test
    @Disabled
    public void testToDtos()
    {
        Gene gene1 = buildGene(ID1);
        Gene gene2 = buildGene(ID2);
        Set<Gene> genes = new HashSet<>();
        genes.add(gene1);
        genes.add(gene2);
        EntityMapper entityMapper = new EntityMapper(modelMapper);
        List<GeneCommonDTO> geneDTOList = entityMapper.toTargets(genes, GeneCommonDTO.class);
        assertThat("geneDTOList size", geneDTOList.size(), is(2));
    }

    private Gene buildGene(Long id)
    {
        Gene gene = new Gene();
        gene.setId(id);
        gene.setAccId(ID_LIST_VALUE);
        gene.setSymbol(SYMBOL + id);
        Species species = new Species();
        species.setName(SPECIES_NAME);
        gene.setSpecies(species);

        return gene;
    }
}