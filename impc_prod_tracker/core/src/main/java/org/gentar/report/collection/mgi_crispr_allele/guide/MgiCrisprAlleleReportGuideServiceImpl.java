package org.gentar.report.collection.mgi_crispr_allele.guide;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportGuideServiceImpl implements MgiCrisprAlleleReportGuideService {

    private final MgiCrisprAlleleReportGuideRepository mgiCrisprAlleleReportGuideRepository;

    public MgiCrisprAlleleReportGuideServiceImpl(
            MgiCrisprAlleleReportGuideRepository mgiCrisprAlleleReportGuideRepository)
    {
        this.mgiCrisprAlleleReportGuideRepository = mgiCrisprAlleleReportGuideRepository;
    }

    @Override
    public List<MgiCrisprAlleleReportGuideProjection> getSelectedGuideProjections(List<Long> planIds) {
        return mgiCrisprAlleleReportGuideRepository.findSelectedGuideProjections(planIds);
    }
}
