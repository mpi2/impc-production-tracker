package org.gentar.report.collection.common.phenotyping.phenotyping_attempt;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl implements CommonPhenotypingColonyReportPhenotypingAttemptService {

    private final CommonPhenotypingColonyReportPhenotypingAttemptRepository commonPhenotypingColonyReportPhenotypingAttemptRepository;

    public CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl(
            CommonPhenotypingColonyReportPhenotypingAttemptRepository commonPhenotypingColonyReportPhenotypingAttemptRepository)
    {
        this.commonPhenotypingColonyReportPhenotypingAttemptRepository = commonPhenotypingColonyReportPhenotypingAttemptRepository;
    }

    public List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections(){
        return  commonPhenotypingColonyReportPhenotypingAttemptRepository.findAllPhenotypingAttemptProjections();
    }
}
