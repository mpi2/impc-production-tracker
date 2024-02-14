package org.gentar.report.export;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.report.collection.work_unit_attempts.WorkUnitAttemptsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/reports")
public class WorkUnitsAttemptsExportController {

    private final WorkUnitAttemptsService workUnitAttemptsService;

    public WorkUnitsAttemptsExportController(WorkUnitAttemptsService workUnitAttemptsService) {
        this.workUnitAttemptsService = workUnitAttemptsService;
    }


    //http://localhost:8080/api/reports/workunit_attempts?attempt=crispr&workunit=IMPC
    @GetMapping("/workunit_attempts")
    @Transactional(readOnly = true)
    public void exportGltAttemptsWithMonth(HttpServletResponse response,
                                           @RequestParam(value = "workunit")
                                               String workUnit,
                                           @RequestParam(value = "attempt")
                                               String attempt)
        throws IOException, ParseException {
        switch (attempt) {
            case "escell" -> attempt = "es cell";
            case "escellallelemodification" -> attempt = "es cell allele modification";
            case "crisprallelemodification" -> attempt = "crispr allele modification";
        }
        workUnitAttemptsService
            .generateWorkUnitAttemptsReport(response, workUnit, attempt);
    }

}