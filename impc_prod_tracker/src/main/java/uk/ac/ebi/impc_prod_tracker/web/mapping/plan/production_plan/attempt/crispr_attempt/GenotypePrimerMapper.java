package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan.attempt.crispr_attempt;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.GenotypePrimerDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class GenotypePrimerMapper
{
    GenotypePrimerDTO toDto(GenotypePrimer genotypePrimer)
    {
        GenotypePrimerDTO genotypePrimerDTO = new GenotypePrimerDTO();
        genotypePrimerDTO.setSequence(genotypePrimer.getSequence());
        genotypePrimerDTO.setName(genotypePrimer.getName());
        genotypePrimerDTO.setStartCoordinate(genotypePrimer.getGenomicStartCoordinate());
        genotypePrimerDTO.setEndCoordinate(genotypePrimer.getGenomicEndCoordinate());

        return genotypePrimerDTO;
    }

    List<GenotypePrimerDTO> toDtos(Collection<GenotypePrimer> genotypePrimers)
    {
        List<GenotypePrimerDTO> genotypePrimerDTOs = new ArrayList<>();
        genotypePrimers.forEach(x -> genotypePrimerDTOs.add(toDto(x)));
        return genotypePrimerDTOs;
    }
}
