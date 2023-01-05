package org.gentar.report.export;

import org.gentar.report.ReportServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportExportController{

    private final ReportServiceImpl reportService;

    public ReportExportController(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = {"/{reportName}"})
    @Transactional(readOnly = true)
    public void export(HttpServletResponse response, @PathVariable String reportName) throws IOException
    {
        reportService.writeReport(response, reportName);
    }
}
