package org.gentar.web.mapping.plan.attempt.crispr_attempt;

import org.gentar.web.dto.plan.production.crispr_attempt.GuideDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.crispr_attempt.guide.Guide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GuideMapper
{
    private ModelMapper modelMapper;

    public GuideMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public GuideDTO toDto(Guide guide)
    {
        GuideDTO guideDTO = modelMapper.map(guide, GuideDTO.class);
        return guideDTO;
    }

    public List<GuideDTO> toDtos(Collection<Guide> guides)
    {
        List<GuideDTO> guideDTOS = new ArrayList<>();
        if (guides != null)
        {
            guides.forEach(guide -> guideDTOS.add(toDto(guide)));
        }
        return guideDTOS;
    }

    public Guide toEntity(GuideDTO guideDTO)
    {
        Guide guide = modelMapper.map(guideDTO, Guide.class);
        return guide;
    }

    public Set<Guide> toEntities(Collection<GuideDTO> guideDTOs)
    {
        Set<Guide> guides = new HashSet<>();
        if (guideDTOs != null)
        {
            guideDTOs.forEach(guideDTO -> guides.add(toEntity(guideDTO)));
        }
        return guides;
    }
}
