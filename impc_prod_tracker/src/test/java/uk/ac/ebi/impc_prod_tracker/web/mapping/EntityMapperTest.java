package uk.ac.ebi.impc_prod_tracker.web.mapping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.species.Species;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.GeneDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
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
    public void testToDto()
    {
        EntityMapper entityMapper = new EntityMapper(modelMapper);
        Gene gene = buildGene(ID1);

        GeneDTO geneDTO = entityMapper.toTarget(gene, GeneDTO.class);

        assertThat("Gene Symbol", geneDTO.getSymbol(), is(SYMBOL + ID1));
        assertThat("Gene idListValue", geneDTO.getIdListValue(), is(ID_LIST_VALUE));
        assertThat("Gene Species Name", geneDTO.getSpeciesName(), is(SPECIES_NAME));
    }

    @Test
    public void testToDtoNull()
    {
        EntityMapper entityMapper = new EntityMapper(modelMapper);
        Gene gene = null;

        GeneDTO geneDTO = entityMapper.toTarget(gene, GeneDTO.class);

        assertThat("geneDTO", geneDTO, is(nullValue()));
    }

    @Test
    public void testToDtos()
    {
        Gene gene1 = buildGene(ID1);
        Gene gene2 = buildGene(ID2);
        Set<Gene> genes = new HashSet<>();
        genes.add(gene1);
        genes.add(gene2);
        EntityMapper entityMapper = new EntityMapper(modelMapper);
        List<GeneDTO> geneDTOList = entityMapper.toTargets(genes, GeneDTO.class);
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