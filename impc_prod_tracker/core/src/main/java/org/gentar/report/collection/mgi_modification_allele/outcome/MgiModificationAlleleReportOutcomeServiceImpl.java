package org.gentar.report.collection.mgi_modification_allele.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
class MgiModificationAlleleReportOutcomeServiceImpl implements MgiModificationAlleleReportOutcomeService{
    private final MgiModificationAlleleReportOutcomeRepository modificationAlleleReportOutcomeRepository;

    MgiModificationAlleleReportOutcomeServiceImpl(MgiModificationAlleleReportOutcomeRepository modificationAlleleReportOutcomeRepository) {
        this.modificationAlleleReportOutcomeRepository = modificationAlleleReportOutcomeRepository;
    }

    @Override
    public List<MgiModificationAlleleReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds ) {
        return modificationAlleleReportOutcomeRepository.findSelectedOutcomeMutationProjections(outcomeIds);
    }
}
