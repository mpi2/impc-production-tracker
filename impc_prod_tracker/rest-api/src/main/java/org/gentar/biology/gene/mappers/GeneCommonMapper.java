package org.gentar.biology.gene.mappers;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneCommonDTO;
import org.gentar.biology.gene.GeneService;
import org.gentar.biology.species.SpeciesNames;
import org.springframework.stereotype.Component;

@Component
public class GeneCommonMapper implements Mapper<Gene, GeneCommonDTO>
{
    private final GeneService geneService;

    public GeneCommonMapper(GeneService geneService)
    {
        this.geneService = geneService;
    }

    @Override
    public GeneCommonDTO toDto(Gene gene)
    {
        GeneCommonDTO geneCommonDTO = new GeneCommonDTO();
        geneCommonDTO.setAccId(gene.getAccId());
        geneCommonDTO.setSymbol(gene.getSymbol());
        if (gene.getSpecies() != null)
        {
            geneCommonDTO.setSpeciesName(gene.getSpecies().getName());
        }
        return geneCommonDTO;
    }

    @Override
    public Gene toEntity(GeneCommonDTO geneCommonDTO)
    {
        return loadGene(geneCommonDTO);
    }

    private Gene loadGene(GeneCommonDTO geneCommonDTO)
    {
        String speciesName = geneCommonDTO.getSpeciesName();
        SpeciesNames speciesNameEnum = SpeciesNames.valueOfLabel(speciesName);
        Gene gene = null;
        String searchTerm = null;
        if (geneCommonDTO.getAccId() != null)
        {
            searchTerm = geneCommonDTO.getAccId();
        }
        else
        {
            searchTerm = geneCommonDTO.getSymbol();
        }
        if (searchTerm != null)
        {
            gene = geneService.findAndCreateInLocalIfNeededFailIfNull(searchTerm, speciesNameEnum);
        }
        return gene;
    }
}
