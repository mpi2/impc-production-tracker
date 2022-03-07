package org.gentar.report.collection.mgi_crispr_allele.mutation_characterization;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportMutationCategorizationServiceImpl
        implements MgiCrisprAlleleReportMutationCategorizationService
{

    private final MgiCrisprAlleleReportMutationCategorizationRepository mutationCategorizationRepository;

    public MgiCrisprAlleleReportMutationCategorizationServiceImpl(
            MgiCrisprAlleleReportMutationCategorizationRepository mutationCategorizationRepository) {
        this.mutationCategorizationRepository = mutationCategorizationRepository;
    }

    @Override
    public List<MgiCrisprAlleleReportMutationCategorizationProjection> getSelectedMutationCategorizationProjections(
            List<Long> mutationIds)
    {
        return mutationCategorizationRepository.findSelectedMutationCharacterizationProjections(mutationIds);
    }
}
