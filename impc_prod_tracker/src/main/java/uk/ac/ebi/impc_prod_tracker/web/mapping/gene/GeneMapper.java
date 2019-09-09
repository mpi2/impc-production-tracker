package uk.ac.ebi.impc_prod_tracker.web.mapping.gene;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.GeneDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GeneMapper
{
    public GeneDTO toDto (Gene gene)
    {
        GeneDTO geneDTO = new GeneDTO();
        geneDTO.setAccessionIdValue(gene.getIdListValue());
        geneDTO.setSymbol(gene.getSymbol());
        geneDTO.setSpecieName(gene.getSpecies().getName());
        return geneDTO;
    }

    public List<GeneDTO> toDtos (Set<Gene> genes)
    {
        List<GeneDTO> geneDTOS = new ArrayList<>();
        genes.forEach(gene -> geneDTOS.add(toDto(gene)));
        return geneDTOS;
    }
}
