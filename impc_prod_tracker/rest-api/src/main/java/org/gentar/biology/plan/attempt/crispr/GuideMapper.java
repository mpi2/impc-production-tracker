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
public class GuideMapper implements Mapper<Guide, GuideDTO> {
    private final EntityMapper entityMapper;
    private final GuideFormatRepository guideFormatRepository;
    private final GuideSourceRepository guideSourceRepository;
    private final AssemblyMapMapper assemblyMapMapper;

    public GuideMapper(EntityMapper entityMapper,
                       GuideFormatRepository guideFormatRepository,
                       GuideSourceRepository guideSourceRepository,
                       AssemblyMapMapper assemblyMapMapper) {
        this.entityMapper = entityMapper;
        this.guideFormatRepository = guideFormatRepository;
        this.guideSourceRepository = guideSourceRepository;
        this.assemblyMapMapper = assemblyMapMapper;
    }

    public GuideDTO toDto(Guide guide) {
        return entityMapper.toTarget(guide, GuideDTO.class);
    }

    public List<GuideDTO> toDtos(Collection<Guide> guides) {
        List<GuideDTO> guideDTOS = new ArrayList<>();
        if (guides != null) {
            guides.forEach(guide -> guideDTOS.add(toDto(guide)));
        }
        return guideDTOS;
    }

    public Guide toEntity(GuideDTO guideDTO, String nucleaseType) {
        if (guideDTO == null) {
            return null;
        }
        
        Guide guide = new Guide();
        
        if (guideDTO.getId() != null && guideDTO.getId() > 0) {
            guide.setId(guideDTO.getId());
        }
        
        String guideSequence = guideDTO.getGuideSequence();
        if (guideSequence != null) {
            guideSequence = guideSequence.replace(" ", "");
        }
        guide.setGuideSequence(guideSequence);
        
        String pam = guideDTO.getPam();
        if (pam != null) {
            pam = pam.replace(" ", "");
        }
        guide.setPam(pam);
        
        String sequence = guideDTO.getSequence();
        if (sequence == null || sequence.isEmpty()) {
            if ("Cpf1".equalsIgnoreCase(nucleaseType)) {
                sequence = (pam != null ? pam : "") + (guideSequence != null ? guideSequence : "");
            } else {
                sequence = (guideSequence != null ? guideSequence : "") + (pam != null ? pam : "");
            }
        }
        guide.setSequence(sequence);
        
        guide.setChr(guideDTO.getChr());
        guide.setStart(guideDTO.getStart());
        guide.setStop(guideDTO.getStop());
        guide.setStrand(guideDTO.getStrand());
        guide.setGenomeBuild(guideDTO.getGenomeBuild());
        guide.setGrnaConcentration(guideDTO.getGrnaConcentration());
        guide.setTruncatedGuide(guideDTO.getTruncatedGuide());
        guide.setReversed(guideDTO.getReversed());
        guide.setSangerService(guideDTO.getSangerService());
        guide.setGid(guideDTO.getGid());

        if (guideDTO.getFormatName() != null) {
            GuideFormat guideFormat =
                    guideFormatRepository.findByNameIgnoreCase(guideDTO.getFormatName());
            guide.setGuideFormat(guideFormat);
        }
        if (guideDTO.getSourceName() != null) {
            GuideSource guideSource =
                    guideSourceRepository.findByNameIgnoreCase(guideDTO.getSourceName());
            guide.setGuideSource(guideSource);
        }

        if (guide.getGenomeBuild() != null && guide.getStart() != null &&
                guide.getStop() != null && guide.getStrand() != null && guide.getChr() != null) {
            if ("GRCm38".equals(guide.getGenomeBuild())) {
                changeAssemblyIfNecessary(guide);
            }
        }

        return guide;
    }

    private void changeAssemblyIfNecessary(Guide guide) {
        assemblyMapMapper.assemblyConvertion(guide);
    }

    public Set<Guide> toEntities(CrisprAttemptDTO crisprAttemptDTO) {
        String nucleaseType = crisprAttemptDTO.getNucleaseDTOS() != null ? crisprAttemptDTO.getNucleaseDTOS().getFirst().getTypeName() : null;
        Collection<GuideDTO> guideDTOs = crisprAttemptDTO.getGuideDTOS();
        Set<Guide> guides = new HashSet<>();
        if (guideDTOs != null) {
            guideDTOs.forEach(guideDTO -> guides.add(toEntity(guideDTO, nucleaseType)));
        }
        return guides;
    }
}