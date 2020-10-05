package org.gentar.biology.gene.mappers;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.GeneCommonDTO;
import org.gentar.biology.gene.GeneResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class GeneResponseMapper implements Mapper<Gene, GeneResponseDTO>
{
    private final GeneCommonMapper geneCommonMapper;

    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";

    public GeneResponseMapper(GeneCommonMapper geneCommonMapper)
    {
        this.geneCommonMapper = geneCommonMapper;
    }

    @Override
    public GeneResponseDTO toDto(Gene gene)
    {
        GeneResponseDTO geneResponseDTO = new GeneResponseDTO();
        geneResponseDTO.setId(gene.getId());
        geneResponseDTO.setName(gene.getName());
        geneResponseDTO.setExternalLink(MGI_URL + gene.getAccId());
        GeneCommonDTO geneCommonDTO = geneCommonMapper.toDto(gene);
        geneResponseDTO.setGeneCommonDTO(geneCommonDTO);
        return geneResponseDTO;
    }

    @Override
    public Gene toEntity(GeneResponseDTO geneResponseDTO)
    {
        // Mapper only used t get the dto.
        return null;
    }
}
