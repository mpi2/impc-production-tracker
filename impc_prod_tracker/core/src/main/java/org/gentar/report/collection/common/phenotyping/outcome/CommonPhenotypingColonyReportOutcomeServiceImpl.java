package org.gentar.report.collection.common.phenotyping.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonPhenotypingColonyReportOutcomeServiceImpl implements CommonPhenotypingColonyReportOutcomeService {

    private final CommonPhenotypingColonyReportOutcomeRepository commonPhenotypingColonyReportOutcomeRepository;

    public CommonPhenotypingColonyReportOutcomeServiceImpl(CommonPhenotypingColonyReportOutcomeRepository commonPhenotypingColonyReportOutcomeRepository) {
        this.commonPhenotypingColonyReportOutcomeRepository = commonPhenotypingColonyReportOutcomeRepository;
    }

    @Override
    public List<CommonPhenotypingColonyReportOutcomeMutationProjection> getSelectedOutcomeMutationProjections(List<Long> outcomeIds ) {
        return commonPhenotypingColonyReportOutcomeRepository.findSelectedOutcomeMutationProjections(outcomeIds);
    }
}
