package org.gentar.report.mgiCrisprAllele;

import org.gentar.report.geneInterest.GeneInterestReportServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class GeneInterestReportController {
    private final GeneInterestReportServiceImpl reportService;

    public GeneInterestReportController( GeneInterestReportServiceImpl reportService ) {
        this.reportService = reportService;
    }


    @GetMapping("/geneInterest")
    public void exportGeneInterest( HttpServletResponse response) throws IOException
    {
        reportService.generateGeneInterestReport();
    }

}
