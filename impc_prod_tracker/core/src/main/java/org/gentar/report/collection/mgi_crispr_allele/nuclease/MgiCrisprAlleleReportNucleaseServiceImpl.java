package org.gentar.report.collection.mgi_crispr_allele.nuclease;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportNucleaseServiceImpl implements MgiCrisprAlleleReportNucleaseService {

    private final MgiCrisprAlleleReportNucleaseRepository mgiCrisprAlleleReportNucleaseRepository;

    public MgiCrisprAlleleReportNucleaseServiceImpl(MgiCrisprAlleleReportNucleaseRepository mgiCrisprAlleleReportNucleaseRepository) {
        this.mgiCrisprAlleleReportNucleaseRepository = mgiCrisprAlleleReportNucleaseRepository;
    }

    @Override
    public List<MgiCrisprAlleleReportNucleaseProjection> getSelectedNucleaseProjections(List planIds) {
        return mgiCrisprAlleleReportNucleaseRepository.findSelectedNucleaseProjections(planIds);
    }
}
