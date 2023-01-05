package org.gentar.report.maintenance;

import org.gentar.report.ReportServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/tracking-api/reports/maintenance")
public class ReportMaintenanceController {

    private final ReportServiceImpl reportService;

    public ReportMaintenanceController(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/cleanAll")
    public void cleanAllDataBaseReports(HttpServletResponse response) throws IOException
    {
        reportService.cleanAllReports();
    }


}
