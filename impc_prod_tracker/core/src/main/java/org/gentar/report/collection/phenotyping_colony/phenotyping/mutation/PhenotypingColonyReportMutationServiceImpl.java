package org.gentar.report.collection.phenotyping_colony.phenotyping.mutation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhenotypingColonyReportMutationServiceImpl implements PhenotypingColonyReportMutationService
{

    private final PhenotypingColonyReportMutationRepository phenotypingColonyReportMutationRepository;

    public PhenotypingColonyReportMutationServiceImpl(
            PhenotypingColonyReportMutationRepository phenotypingColonyReportMutationRepository)
    {
        this.phenotypingColonyReportMutationRepository = phenotypingColonyReportMutationRepository;
    }


    @Override
    public List<PhenotypingColonyReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds){
        return phenotypingColonyReportMutationRepository.findSelectedMutationGeneProjections(mutationIds);
    }
}
