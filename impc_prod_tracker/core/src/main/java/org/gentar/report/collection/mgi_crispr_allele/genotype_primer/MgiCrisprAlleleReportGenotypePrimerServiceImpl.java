package org.gentar.report.collection.mgi_crispr_allele.genotype_primer;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportGenotypePrimerServiceImpl implements MgiCrisprAlleleReportGenotypePrimerService {
    private final MgiCrisprAlleleReportGenotypePrimerRepository genotypePrimerRepository;

    public MgiCrisprAlleleReportGenotypePrimerServiceImpl(MgiCrisprAlleleReportGenotypePrimerRepository genotypePrimerRepository) {
        this.genotypePrimerRepository = genotypePrimerRepository;
    }

    @Override
    public List<MgiCrisprAlleleReportGenotypePrimerProjection> getSelectedGenotypePrimerProjections(List<Long> planIds) {
        return genotypePrimerRepository.findSelectedGenotypePrimerProjections(planIds);
    }
}
