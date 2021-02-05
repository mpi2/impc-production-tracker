package org.gentar.report.collection.common.phenotyping.mutation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonPhenotypingColonyReportMutationServiceImpl implements CommonPhenotypingColonyReportMutationService
{

    private final CommonPhenotypingColonyReportMutationRepository commonPhenotypingColonyReportMutationRepository;

    public CommonPhenotypingColonyReportMutationServiceImpl(
            CommonPhenotypingColonyReportMutationRepository commonPhenotypingColonyReportMutationRepository)
    {
        this.commonPhenotypingColonyReportMutationRepository = commonPhenotypingColonyReportMutationRepository;
    }


    public List<CommonPhenotypingColonyReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds){
        return commonPhenotypingColonyReportMutationRepository.findSelectedMutationGeneProjections(mutationIds);
    }
}
