package org.gentar.report.collection.phenotyping_colony.phenotyping.phenotyping_attempt;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PhenotypingColonyReportPhenotypingAttemptServiceImpl implements PhenotypingColonyReportPhenotypingAttemptService {

    private final PhenotypingColonyReportPhenotypingAttemptRepository phenotypingColonyReportPhenotypingAttemptRepository;

    public PhenotypingColonyReportPhenotypingAttemptServiceImpl(
            PhenotypingColonyReportPhenotypingAttemptRepository phenotypingColonyReportPhenotypingAttemptRepository)
    {
        this.phenotypingColonyReportPhenotypingAttemptRepository = phenotypingColonyReportPhenotypingAttemptRepository;
    }

    public List<PhenotypingColonyReportPhenotypingAttemptProjection> getPhenotypingAttemptProjections(){

        List<PhenotypingColonyReportPhenotypingAttemptProjection>
                crisprProjections = getCrisprPhenotypingAttemptProjections();

        List<PhenotypingColonyReportPhenotypingAttemptProjection>
                esCellProjections = getEsCellPhenotypingAttemptProjections();

        List<PhenotypingColonyReportPhenotypingAttemptProjection>
                esCellModificationProjections = getEsCellModificationPhenotypingAttemptProjections();

        return Stream.of(crisprProjections,
                esCellProjections,
                esCellModificationProjections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }

    public List<PhenotypingColonyReportPhenotypingAttemptProjection> getCrisprPhenotypingAttemptProjections(){
        return  phenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromCrisprProduction();
    }

    public List<PhenotypingColonyReportPhenotypingAttemptProjection> getEsCellPhenotypingAttemptProjections(){
        return  phenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromEsCellProduction();
    }

    public List<PhenotypingColonyReportPhenotypingAttemptProjection> getEsCellModificationPhenotypingAttemptProjections(){
        return  phenotypingColonyReportPhenotypingAttemptRepository.findPhenotypingAttemptProjectionsFromEsCellModificationProduction();
    }
}
