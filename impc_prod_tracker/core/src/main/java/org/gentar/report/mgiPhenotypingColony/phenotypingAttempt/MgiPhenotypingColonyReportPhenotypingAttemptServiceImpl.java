package org.gentar.report.mgiPhenotypingColony.phenotypingAttempt;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl implements MgiPhenotypingColonyReportPhenotypingAttemptService {

    private final MgiPhenotypingColonyReportPhenotypingAttemptRepository mgiPhenotypingColonyReportPhenotypingAttemptRepository;

    public MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl(
            MgiPhenotypingColonyReportPhenotypingAttemptRepository mgiPhenotypingColonyReportPhenotypingAttemptRepository )
    {
        this.mgiPhenotypingColonyReportPhenotypingAttemptRepository = mgiPhenotypingColonyReportPhenotypingAttemptRepository;
    }

    public List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjectionsForMgi(){
        return  mgiPhenotypingColonyReportPhenotypingAttemptRepository.findAllPhenotypingAttemptProjectionsForMgi();
    }
}
