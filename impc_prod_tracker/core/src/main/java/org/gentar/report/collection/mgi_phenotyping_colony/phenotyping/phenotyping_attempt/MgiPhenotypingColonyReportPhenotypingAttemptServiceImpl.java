package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.phenotyping_attempt;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl {
    private final MgiPhenotypingColonyReportPhenotypingAttemptRepository mgiPhenotypingColonyReportPhenotypingAttemptRepository;
    public MgiPhenotypingColonyReportPhenotypingAttemptServiceImpl(MgiPhenotypingColonyReportPhenotypingAttemptRepository mgiPhenotypingColonyReportPhenotypingAttemptRepository){
        this.mgiPhenotypingColonyReportPhenotypingAttemptRepository = mgiPhenotypingColonyReportPhenotypingAttemptRepository;
    }

    public List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections(){

        List<MgiPhenotypingColonyReportPhenotypingAttemptProjection>
                crisprProjections = getCrisprPhenotypingAttemptProjections();

        List<MgiPhenotypingColonyReportPhenotypingAttemptProjection>
                esCellProjections = getEsCellPhenotypingAttemptProjections();

        List<MgiPhenotypingColonyReportPhenotypingAttemptProjection>
                esCellModificationProjections = getEsCellModificationPhenotypingAttemptProjections();

        return Stream.of(crisprProjections,
                        esCellProjections,
                        esCellModificationProjections)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

    }

    public List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getCrisprPhenotypingAttemptProjections(){
        return  mgiPhenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromCrisprProduction();
    }

    public List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getEsCellPhenotypingAttemptProjections(){
        return  mgiPhenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromEsCellProduction();
    }

    public List<MgiPhenotypingColonyReportPhenotypingAttemptProjection> getEsCellModificationPhenotypingAttemptProjections(){
        return  mgiPhenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromEsCellModificationProduction();
    }
}
