package org.gentar.report.mgiCrisprAllele.colony;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MgiCrisprAlleleReportColonyServiceImpl implements MgiCrisprAlleleReportColonyService
{

    private final MgiCrisprAlleleReportColonyRepository mgiCrisprAlleleReportColonyRepository;

    public MgiCrisprAlleleReportColonyServiceImpl( MgiCrisprAlleleReportColonyRepository mgiCrisprAlleleReportColonyRepository ) {
        this.mgiCrisprAlleleReportColonyRepository = mgiCrisprAlleleReportColonyRepository;
    }


    public List<MgiCrisprAlleleReportColonyProjection> getAllColonyReportProjections() {
        return mgiCrisprAlleleReportColonyRepository.findAllColonyReportProjections();
    }
}
