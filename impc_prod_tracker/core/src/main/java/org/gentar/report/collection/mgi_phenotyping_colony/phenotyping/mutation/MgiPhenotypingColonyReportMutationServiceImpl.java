package org.gentar.report.collection.mgi_phenotyping_colony.phenotyping.mutation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiPhenotypingColonyReportMutationServiceImpl implements MgiPhenotypingColonyReportMutationService
{
    private final MgiPhenotypingColonyReportMutationRepository mgiPhenotypingColonyReportMutationRepository;

    public MgiPhenotypingColonyReportMutationServiceImpl(
            MgiPhenotypingColonyReportMutationRepository mgiPhenotypingColonyReportMutationRepository) {
        this.mgiPhenotypingColonyReportMutationRepository = mgiPhenotypingColonyReportMutationRepository;
    }

    @Override
    public List<MgiPhenotypingColonyReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds) {
        return mgiPhenotypingColonyReportMutationRepository.findSelectedMutationGeneProjections(mutationIds);
    }
}
