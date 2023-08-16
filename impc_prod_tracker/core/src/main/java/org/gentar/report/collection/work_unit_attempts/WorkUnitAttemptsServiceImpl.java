package org.gentar.report.collection.work_unit_attempts;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import org.gentar.report.Report;
import org.gentar.report.ReportService;
import org.gentar.report.collection.work_unit_attempts.projection.WorkUnitAttemptsProjection;
import org.gentar.report.collection.work_unit_attempts.repository.WorkUnitAttemptsRepository;
import org.springframework.stereotype.Component;

@Component
public class WorkUnitAttemptsServiceImpl implements WorkUnitAttemptsService {

    private final ReportService reportService;
    private final WorkUnitAttemptsRepository workUnitAttemptsRepository;

    public WorkUnitAttemptsServiceImpl(ReportService reportService,
                                       WorkUnitAttemptsRepository workUnitAttemptsRepository) {
        this.reportService = reportService;

        this.workUnitAttemptsRepository = workUnitAttemptsRepository;
    }


    @Override
    public void generateWorkUnitAttemptsReport(HttpServletResponse response, String workUnit,
                                               String attempt) throws IOException, ParseException {
        List<WorkUnitAttemptsProjection> workUnitAttemptsProjections;


        workUnitAttemptsProjections = workUnitAttemptsRepository
            .findAttemptsByWorkUnitAndAttempt(workUnit, attempt);

        formatProjectionReportText(response, workUnit, attempt,
            workUnitAttemptsProjections);
    }


    private void formatProjectionReportText(HttpServletResponse response,
                                            String workUnit,
                                            String attempt,
                                            List<WorkUnitAttemptsProjection> workUnitAttemptsProjections)
        throws IOException {
        StringBuilder reportText = new StringBuilder();

        reportText.append("Date\tName\tMin\tMutation Symbol\tGene Symbol\n");

        for (WorkUnitAttemptsProjection workUnitAttemptsProjection : workUnitAttemptsProjections) {
            reportText.append(workUnitAttemptsProjection.getDate()).append("\t");
            reportText.append(workUnitAttemptsProjection.getName()).append("\t");
            reportText.append(workUnitAttemptsProjection.getMin()).append("\t");
            reportText.append(workUnitAttemptsProjection.getMutationSymbol()).append("\t");
            reportText.append(workUnitAttemptsProjection.getGeneSymbol()).append("\t");
            reportText.append("\n");
        }

        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
            .writeReport(response, attempt+"_Attempt_by_" + workUnit ,
                report);
    }

}
