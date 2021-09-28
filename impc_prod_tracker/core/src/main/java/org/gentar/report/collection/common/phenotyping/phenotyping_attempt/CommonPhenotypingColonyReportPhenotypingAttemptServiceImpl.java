package org.gentar.report.collection.common.phenotyping.phenotyping_attempt;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl implements CommonPhenotypingColonyReportPhenotypingAttemptService {

    private final CommonPhenotypingColonyReportPhenotypingAttemptRepository commonPhenotypingColonyReportPhenotypingAttemptRepository;

    public CommonPhenotypingColonyReportPhenotypingAttemptServiceImpl(
            CommonPhenotypingColonyReportPhenotypingAttemptRepository commonPhenotypingColonyReportPhenotypingAttemptRepository)
    {
        this.commonPhenotypingColonyReportPhenotypingAttemptRepository = commonPhenotypingColonyReportPhenotypingAttemptRepository;
    }

    public List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections(){

        List<CommonPhenotypingColonyReportPhenotypingAttemptProjection>
                crisprProjections = getCrisprPhenotypingAttemptProjections();

        List<CommonPhenotypingColonyReportPhenotypingAttemptProjection>
                esCellProjections = getEsCellPhenotypingAttemptProjections();

        List<CommonPhenotypingColonyReportPhenotypingAttemptProjection>
                esCellModificationProjections = getEsCellModificationPhenotypingAttemptProjections();

        return Stream.of(crisprProjections,
                esCellProjections,
                esCellModificationProjections)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

    }

    public List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getCrisprPhenotypingAttemptProjections(){
        return  commonPhenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromCrisprProduction();
    }

    public List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getEsCellPhenotypingAttemptProjections(){
        return  commonPhenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromEsCellProduction();
    }

    public List<CommonPhenotypingColonyReportPhenotypingAttemptProjection> getEsCellModificationPhenotypingAttemptProjections(){
        return  commonPhenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromEsCellModificationProduction();
    }
}
