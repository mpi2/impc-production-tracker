package org.gentar.report.collection.mgi_crispr_allele.outcome;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportOutcomeServiceImpl implements MgiCrisprAlleleReportOutcomeService {

    private final MgiCrisprAlleleReportOutcomeRepository mgiCrisprAlleleReportOutcomeRepository;

    public MgiCrisprAlleleReportOutcomeServiceImpl( MgiCrisprAlleleReportOutcomeRepository mgiCrisprAlleleReportOutcomeRepository ) {
        this.mgiCrisprAlleleReportOutcomeRepository = mgiCrisprAlleleReportOutcomeRepository;
    }


    public List<MgiCrisprAlleleReportOutcomeMutationProjection> getSelectedOutcomeMutationCrisprReportProjections( List<Long> outcomeIds ) {
        return mgiCrisprAlleleReportOutcomeRepository.findSelectedOutcomeMutationCrisprReportProjections(outcomeIds);
    }
}
