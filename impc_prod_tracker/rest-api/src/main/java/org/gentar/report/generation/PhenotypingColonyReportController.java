package org.gentar.report.generation;

import org.gentar.report.collection.phenotyping_colony.PhenotypingColonyReportServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/tracking-api/reports/generate")
public class PhenotypingColonyReportController {
    private final PhenotypingColonyReportServiceImpl phenotypingColonyReportService;

    public PhenotypingColonyReportController(PhenotypingColonyReportServiceImpl phenotypingColonyReportService) {
        this.phenotypingColonyReportService = phenotypingColonyReportService;
    }


    @GetMapping("/phenotyping_colonies")
    public void exportCsv(HttpServletResponse response) throws IOException
    {
        phenotypingColonyReportService.generatePhenotypingColonyReport();
    }

}
