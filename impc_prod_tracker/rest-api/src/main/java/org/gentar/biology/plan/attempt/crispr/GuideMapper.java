package org.gentar.biology.plan.attempt.crispr;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.crispr.guide.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GuideMapper implements Mapper<Guide, GuideDTO>
{
    private EntityMapper entityMapper;
    private CrisprAttemptService crisprAttemptService;
    private GuideFormatRepository guideFormatRepository;
    private GuideSourceRepository guideSourceRepository;
    private AssemblyMapMapper assemblyMapMapper;

    public GuideMapper(EntityMapper entityMapper,
                       CrisprAttemptService crisprAttemptService,
                       GuideFormatRepository guideFormatRepository,
                       GuideSourceRepository guideSourceRepository,
                       AssemblyMapMapper assemblyMapMapper)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptService = crisprAttemptService;
        this.guideFormatRepository = guideFormatRepository;
        this.guideSourceRepository = guideSourceRepository;
        this.assemblyMapMapper = assemblyMapMapper;
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
        if (guide.getSequence() == null && guide.getGuideSequence() != null && guide.getPam() != null)
        {
            guide.setSequence(guideDTO.getGuideSequence() + guideDTO.getPam());
        }

        if (guideDTO.getFormatName() != null)
        {
            GuideFormat guideFormat = guideFormatRepository.findByNameIgnoreCase(guideDTO.getFormatName());
            guide.setGuideFormat(guideFormat);
        }
        if (guideDTO.getSourceName() != null)
        {
            GuideSource guideSource = guideSourceRepository.findByNameIgnoreCase(guideDTO.getSourceName());
            guide.setGuideSource(guideSource);
        }

        if (guide.getGenomeBuild().equals("GRCm38"))
        {
            changeAssemblyIfNecessary(guide);
        }

        return guide;
    }

    private void changeAssemblyIfNecessary(Guide guide)
    {
        assemblyMapMapper.assemblyConvertion(guide);
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
