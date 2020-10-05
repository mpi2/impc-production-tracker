package org.gentar.biology.gene.mappers;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class GeneUpdateMapper implements Mapper<Gene, GeneUpdateDTO>
{
    private final GeneCommonMapper geneCommonMapper;

    public GeneUpdateMapper(GeneCommonMapper geneCommonMapper)
    {
        this.geneCommonMapper = geneCommonMapper;
    }

    @Override
    public GeneUpdateDTO toDto(Gene entity)
    {
        return null;
    }

    @Override
    public Gene toEntity(GeneUpdateDTO geneUpdateDTO)
    {
        Gene gene = null;
        if (geneUpdateDTO.getGeneCommonDTO() != null)
        {
            gene = geneCommonMapper.toEntity(geneUpdateDTO.getGeneCommonDTO());
        }
        return gene;
    }
}
