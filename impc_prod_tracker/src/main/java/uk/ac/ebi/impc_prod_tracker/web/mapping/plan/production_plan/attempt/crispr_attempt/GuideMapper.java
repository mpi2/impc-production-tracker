package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan.attempt.crispr_attempt;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.guide.Guide;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.GuideDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
class GuideMapper
{
    private ModelMapper modelMapper;

    GuideMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    GuideDTO toDto(Guide guide)
    {
        GuideDTO guideDTO = new GuideDTO();
        modelMapper.map(guide, guideDTO);
        return guideDTO;
    }

    List<GuideDTO> toDtos(Set<Guide> guides)
    {
        List<GuideDTO> guideDTOs = new ArrayList<>();
        guides.forEach(guide -> guideDTOs.add(toDto(guide)));
        return guideDTOs;
    }
}
