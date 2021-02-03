package org.gentar.report.report_type;

import org.gentar.report.ReportTypeServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class PublicReportTypeController {

    private final ReportTypeServiceImpl reportTypeService;

    public PublicReportTypeController(ReportTypeServiceImpl reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @GetMapping(value ="/list")
    public List<String> listPublicReportTypes()
    {
        return reportTypeService.listPublicReportTypes();
    }
}
