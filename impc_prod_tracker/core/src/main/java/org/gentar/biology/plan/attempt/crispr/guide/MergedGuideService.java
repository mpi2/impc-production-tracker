package org.gentar.biology.plan.attempt.crispr.guide;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MergedGuideService {


    private final MergedGuideRepository mergedGuideRepository;

    public MergedGuideService(MergedGuideRepository mergedGuideRepository) {
        this.mergedGuideRepository = mergedGuideRepository;
    }

    @Transactional
    public void saveNonExistingGuides(List<Guide> nonExistingGuides) {

        List<MergedGuide> mergedGuides = nonExistingGuides.stream()
                .map(this::mapToMergedGuide)
                .collect(Collectors.toList());
        mergedGuideRepository.saveAll(mergedGuides);
    }

    private MergedGuide mapToMergedGuide(Guide guide) {
        MergedGuide mergedGuide = new MergedGuide();
        mergedGuide.setChr(guide.getChr());
        mergedGuide.setGenomeBuild(guide.getGenomeBuild());
        mergedGuide.setGid(guide.getGid());
        mergedGuide.setGrnaConcentration(guide.getGrnaConcentration());
        mergedGuide.setGuideSequence(guide.getGuideSequence());
        mergedGuide.setPam(guide.getPam());
        mergedGuide.setReversed(guide.getReversed());
        mergedGuide.setSangerService(guide.getSangerService());
        mergedGuide.setSequence(guide.getSequence());
        mergedGuide.setStart(guide.getStart());
        mergedGuide.setStop(guide.getStop());
        mergedGuide.setStrand(guide.getStrand());
        mergedGuide.setTruncatedGuide(guide.getTruncatedGuide());
        mergedGuide.setCrisprAttempt(guide.getCrisprAttempt());
        mergedGuide.setGuideFormat(guide.getGuideFormat());
        mergedGuide.setGuideSource(guide.getGuideSource());
        mergedGuide.setCreatedAt(guide.getCreatedAt());
        mergedGuide.setCreatedBy(guide.getCreatedBy());
        mergedGuide.setLastModified(guide.getLastModified());
        mergedGuide.setLastModifiedBy(guide.getLastModifiedBy());
        return mergedGuide;
    }
}
