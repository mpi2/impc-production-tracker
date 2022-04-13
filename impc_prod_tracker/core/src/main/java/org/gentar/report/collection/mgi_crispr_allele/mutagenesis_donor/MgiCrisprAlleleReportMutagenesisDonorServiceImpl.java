package org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportMutagenesisDonorServiceImpl implements MgiCrisprAlleleReportMutagenesisDonorService{

    private final MgiCrisprAlleleReportMutagenesisDonorRepository mutagenesisDonorRepository;

    public MgiCrisprAlleleReportMutagenesisDonorServiceImpl(MgiCrisprAlleleReportMutagenesisDonorRepository mutagenesisDonorRepository) {
        this.mutagenesisDonorRepository = mutagenesisDonorRepository;
    }

    @Override
    public List<MgiCrisprAlleleReportMutagenesisDonorProjection> getSelectedMutagenesisDonorProjections(List planIds) {
        return mutagenesisDonorRepository.findSelectedMutagenesisDonorProjections(planIds);
    }
}
