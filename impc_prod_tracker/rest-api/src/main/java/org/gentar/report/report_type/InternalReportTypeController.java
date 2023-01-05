package org.gentar.report.report_type;

import org.gentar.report.ReportTypeServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tracking-api/reports/types")
public class InternalReportTypeController {

    private final ReportTypeServiceImpl reportTypeService;

    public InternalReportTypeController(ReportTypeServiceImpl reportTypeService) {
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

    @GetMapping("/updateAllDescriptions")
    public void updateAllReportTypeDescriptions(HttpServletResponse response) throws IOException
    {
        reportTypeService.updateAllReportTypeDescriptions();
    }

    @GetMapping(value = {"/updateDescription/{reportType}"})
    public void updateReportTypeDescription(
            HttpServletResponse response, @PathVariable String reportType) throws IOException
    {
        reportTypeService.updateReportTypeDescription(response, reportType);
    }

    @GetMapping("/updatePublicSettings")
    public void updateAllReportTypePublicSettings(HttpServletResponse response) throws IOException
    {
        reportTypeService.updateAllReportTypePublicSettings();
    }
}

