package org.gentar.report.report_type;

import org.gentar.report.ReportTypeServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/tracking-api/reports/types")
public class ReportTypeController {

    private final ReportTypeServiceImpl reportTypeService;

    public ReportTypeController(ReportTypeServiceImpl reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @GetMapping("/createAll")
    public void createAllReportTypes(HttpServletResponse response) throws IOException
    {
        reportTypeService.createAllReportTypes();
    }

    @GetMapping(value = {"/create/{reportType}"})
    public void createSpecificReportType(
            HttpServletResponse response, @PathVariable String reportType) throws IOException
    {
        reportTypeService.createSpecificReportType(response, reportType);
    }

    @GetMapping("/updateAll")
    public void updateAllReportTypeDescriptions(HttpServletResponse response) throws IOException
    {
        reportTypeService.updateAllReportTypeDescriptions();
    }

    @GetMapping(value = {"/update/{reportType}"})
    public void updateReportTypeDescription(
            HttpServletResponse response, @PathVariable String reportType) throws IOException
    {
        reportTypeService.updateReportTypeDescription(response, reportType);
    }
}

