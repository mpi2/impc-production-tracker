package org.gentar.biology.gene.mappers;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneCreationDTO;
import org.springframework.stereotype.Component;

@Component
public class GeneCreationMapper implements Mapper<Gene, GeneCreationDTO>
{
    private final GeneCommonMapper geneCommonMapper;

    public GeneCreationMapper(GeneCommonMapper geneCommonMapper)
    {
        this.geneCommonMapper = geneCommonMapper;
    }

    @Override
    public GeneCreationDTO toDto(Gene entity)
    {
        return null;
    }

    @Override
    public Gene toEntity(GeneCreationDTO geneCreationDTO)
    {
        Gene gene = null;
        if (geneCreationDTO.getGeneCommonDTO() != null)
        {
            gene = geneCommonMapper.toEntity(geneCreationDTO.getGeneCommonDTO());
        }
        return gene;
    }
}
