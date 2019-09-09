package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt.NucleaseDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class NucleaseMapper
{

    private ModelMapper modelMapper;

    public NucleaseMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public NucleaseDTO toDto(Nuclease nuclease)
    {
        NucleaseDTO nucleaseDTO = modelMapper.map(nuclease, NucleaseDTO.class);
        return nucleaseDTO;
    }

    public List<NucleaseDTO> toDtos(Collection<Nuclease> guides)
    {
        List<NucleaseDTO> guideDTOS = new ArrayList<>();
        if (guides != null)
        {
            guides.forEach(guide -> guideDTOS.add(toDto(guide)));
        }
        return guideDTOS;
    }

    public Nuclease toEntity(NucleaseDTO nucleaseDTO)
    {
        Nuclease nuclease = modelMapper.map(nucleaseDTO, Nuclease.class);
        if (nucleaseDTO.getId() != null)
        {
            nuclease.setId(nucleaseDTO.getId());
        }
        return nuclease;
    }

    public Set<Nuclease> toEntities(Collection<NucleaseDTO> nucleaseDTOS)
    {
        Set<Nuclease> nucleases = new HashSet<>();
        if (nucleaseDTOS != null)
        {
            nucleaseDTOS.forEach(nucleaseDTO -> nucleases.add(toEntity(nucleaseDTO)));
        }
        return nucleases;
    }
}
