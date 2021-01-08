package org.gentar.report.mgiCrisprAllele;

import org.gentar.report.mgiPhenotypingColony.MgiPhenotypingColonyReportServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class MgiReportController {

    private final MgiCrisprAlleleReportServiceImpl mgiCrisprAlleleService;
    private final MgiPhenotypingColonyReportServiceImpl mgiPhenotypingColonyReportService;

    public MgiReportController( MgiCrisprAlleleReportServiceImpl mgiCrisprAlleleService,
                                MgiPhenotypingColonyReportServiceImpl mgiPhenotypingColonyReportService ){
        this.mgiCrisprAlleleService = mgiCrisprAlleleService;
        this.mgiPhenotypingColonyReportService = mgiPhenotypingColonyReportService;
    }


    @GetMapping("/mgi/phenotypingColonies")
    public void exportCsv(HttpServletResponse response) throws IOException
    {
        mgiPhenotypingColonyReportService.generateMgiPhenotypingColonyReport();
    }


    @GetMapping("/mgi/crisprAlleles")
    public void exportCrisprAlleles(HttpServletResponse response) throws IOException
    {
        mgiCrisprAlleleService.generateMgiCrisprAlleleReport();
    }
}
