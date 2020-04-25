package org.gentar.biology.plan.attempt.crispr;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
<<<<<<< Updated upstream
=======
import org.modelmapper.ModelMapper;
>>>>>>> Stashed changes
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.crispr.guide.Guide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GuideMapper implements Mapper<Guide, GuideDTO>
{
    private EntityMapper entityMapper;

    public GuideMapper(EntityMapper entityMapper)
    {
        this.entityMapper = entityMapper;
    }

    public GuideDTO toDto(Guide guide)
    {
        GuideDTO guideDTO = entityMapper.toTarget(guide, GuideDTO.class);
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
        Guide guide = entityMapper.toTarget(guideDTO, Guide.class);
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
