package org.gentar.report;

import org.springframework.stereotype.Component;

@Component
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl( ReportRepository reportRepository ) {
        this.reportRepository = reportRepository;
    }
}
