package org.gentar.report.export;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import org.gentar.report.collection.glt_attempts.GltAttemptsServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class GltAttemptsController {

    private final GltAttemptsServiceImpl gltAttemptsService;

    public GltAttemptsController(GltAttemptsServiceImpl gltAttemptsService) {
        this.gltAttemptsService = gltAttemptsService;
    }


    //http://localhost:8080/api/reports/glt_production_numbers?attempt=crispr&workunit=IMPC&startingyear=2016&endingyear=2023
    @GetMapping("/glt_production_numbers")
    @Transactional(readOnly = true)
    public void exportGltAttemptsWithMonth(HttpServletResponse response,
                                           @RequestParam(value = "reporttype")
                                               String reportType,
                                           @RequestParam(value = "attempt")
                                               String attempt,
                                           @RequestParam(value = "workunit", required = false)
                                               String workUnit,
                                           @RequestParam(value = "workGroup", required = false)
                                               String workGroup,
                                           @RequestParam(value = "startyear", required = false)
                                               String startYear,
                                           @RequestParam(value = "endyear", required = false)
                                               String endYear,
                                           @RequestParam(value = "startmonth", required = false)
                                               String starMonth,
                                           @RequestParam(value = "endmonth", required = false)
                                               String endMonth)
        throws IOException, ParseException {
        if (attempt.equals("escell")) {
            attempt = "es cell";
        }
        gltAttemptsService
            .generateGltAttemptsReport(response, reportType, attempt, workUnit, workGroup,
                startYear, endYear, starMonth, endMonth);
    }

    //http://localhost:8080/api/reports/glt_production_numbers/intersection
    @GetMapping("/glt_production_numbers/intersection")
    @Transactional(readOnly = true)
    public void exportGltAttemptsIntersection(HttpServletResponse response) throws IOException {
        gltAttemptsService
            .generateGltAttemptsIntersectionReport(response);
    }

    //http://localhost:8080/api/reports/glt_production_numbers/union
    @GetMapping("/glt_production_numbers/union")
    @Transactional(readOnly = true)
    public void exportGltAttemptsUnion(HttpServletResponse response) throws IOException {
        gltAttemptsService
            .generateGltAttemptsUnionReport(response);
    }
}