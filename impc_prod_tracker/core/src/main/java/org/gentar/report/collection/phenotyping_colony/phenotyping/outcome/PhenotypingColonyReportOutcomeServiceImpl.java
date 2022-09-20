package org.gentar.report.collection.phenotyping_colony.phenotyping.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhenotypingColonyReportOutcomeServiceImpl implements PhenotypingColonyReportOutcomeService {

    private final PhenotypingColonyReportOutcomeRepository phenotypingColonyReportOutcomeRepository;

    public PhenotypingColonyReportOutcomeServiceImpl(PhenotypingColonyReportOutcomeRepository phenotypingColonyReportOutcomeRepository) {
        this.phenotypingColonyReportOutcomeRepository = phenotypingColonyReportOutcomeRepository;
    }

    @Override
    public List<PhenotypingColonyReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds ) {
        return phenotypingColonyReportOutcomeRepository.findSelectedOutcomeMutationProjections(outcomeIds);
    }
}
