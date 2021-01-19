package org.gentar.report;

import org.springframework.stereotype.Component;

@Component
public class ReportTypeServiceImpl implements ReportTypeService {

    private final ReportTypeRepository reportTypeRepository;

    public ReportTypeServiceImpl( ReportTypeRepository reportTypeRepository ) {
        this.reportTypeRepository = reportTypeRepository;
    }
}
