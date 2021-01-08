package org.gentar.report.mgiCrisprAllele.mutation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportMutationServiceImpl implements MgiCrisprAlleleReportMutationService
{

    private final MgiCrisprAlleleReportMutationRepository mgiCrisprAlleleReportMutationRepository;

    public MgiCrisprAlleleReportMutationServiceImpl( MgiCrisprAlleleReportMutationRepository mgiCrisprAlleleReportMutationRepository )
    {
        this.mgiCrisprAlleleReportMutationRepository = mgiCrisprAlleleReportMutationRepository;
    }


    public List<MgiCrisprAlleleReportMutationGeneProjection> getSelectedMutationGeneProjections( List mutationIds){
        return mgiCrisprAlleleReportMutationRepository.findSelectedMutationGeneProjections(mutationIds);
    }
}
