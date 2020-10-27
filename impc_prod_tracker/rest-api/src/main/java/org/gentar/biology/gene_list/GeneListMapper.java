package org.gentar.biology.gene_list;

import org.gentar.Mapper;
import org.springframework.stereotype.Component;

@Component
public class GeneListMapper implements Mapper<GeneList, GeneListDTO>
{
    @Override
    public GeneListDTO toDto(GeneList geneList)
    {
        GeneListDTO geneListDTO = new GeneListDTO();
        geneListDTO.setConsortiumName(geneList.getConsortium().getName());
        geneListDTO.setDescription(geneList.getDescription());
        return geneListDTO;
    }

    @Override
    public GeneList toEntity(GeneListDTO dto)
    {
        return null;
    }
}
