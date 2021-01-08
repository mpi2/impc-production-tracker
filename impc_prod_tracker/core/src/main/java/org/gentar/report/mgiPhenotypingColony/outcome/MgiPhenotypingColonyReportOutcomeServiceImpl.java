package org.gentar.report.mgiPhenotypingColony.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiPhenotypingColonyReportOutcomeServiceImpl implements MgiPhenotypingColonyReportOutcomeService {

    private final MgiPhenotypingColonyReportOutcomeRepository mgiPhenotypingColonyReportOutcomeRepository;

    public MgiPhenotypingColonyReportOutcomeServiceImpl( MgiPhenotypingColonyReportOutcomeRepository mgiPhenotypingColonyReportOutcomeRepository ) {
        this.mgiPhenotypingColonyReportOutcomeRepository = mgiPhenotypingColonyReportOutcomeRepository;
    }


    public List<MgiPhenotypingColonyReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections( List<Long> outcomeIds ) {
        return mgiPhenotypingColonyReportOutcomeRepository.findSelectedOutcomeMutationProjections(outcomeIds);
    }
}
