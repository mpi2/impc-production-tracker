package org.gentar.report.export;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import org.gentar.report.collection.glt_attempts.GltAttemptsServiceImpl;
import org.gentar.report.collection.work_unit_attempts.WorkUnitAttemptsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                           @RequestParam(value = "workunit", required = true)
                                               String workUnit,
                                           @RequestParam(value = "attempt", required = true)
                                               String attempt)
        throws IOException, ParseException {
        if (attempt.equals("escell")) {
            attempt = "es cell";
        }  else if (attempt.equals("escellallelemodification")) {
            attempt = "es cell allele modification";
        } else if (attempt.equals("crisprallelemodification")) {
            attempt = "crispr allele modification";
        }
        workUnitAttemptsService
            .generateWorkUnitAttemptsReport(response, workUnit, attempt);
    }

}