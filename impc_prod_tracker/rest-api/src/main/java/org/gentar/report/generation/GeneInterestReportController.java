package org.gentar.report.generation;

import org.gentar.report.collection.gene_interest.GeneInterestReportServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/tracking-api/reports/generate/")
public class GeneInterestReportController {

    private final GeneInterestReportServiceImpl geneInterestReportService;

    public GeneInterestReportController( GeneInterestReportServiceImpl geneInterestReportService ) {
        this.geneInterestReportService = geneInterestReportService;
    }


    @GetMapping("/gene_interest")
    public void exportGeneInterest( HttpServletResponse response) throws IOException
    {
        geneInterestReportService.generateGeneInterestReport();
    }

}
