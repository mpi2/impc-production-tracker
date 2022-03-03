package org.gentar.report.collection.mgi_crispr_allele.sequence;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportMutationSequenceServiceImpl implements MgiCrisprAlleleReportMutationSequenceService {

    private final MgiCrisprAlleleReportMutationSequenceRepository mutationSequenceRepository;

    public MgiCrisprAlleleReportMutationSequenceServiceImpl(MgiCrisprAlleleReportMutationSequenceRepository mutationSequenceRepository) {
        this.mutationSequenceRepository = mutationSequenceRepository;
    }

    @Override
    public List<MgiCrisprAlleleReportMutationSequenceProjection> getSelectedMutationSequenceProjections(List mutationIds) {
        return mutationSequenceRepository.findSelectedMutationSequenceProjections(mutationIds);
    }
}
