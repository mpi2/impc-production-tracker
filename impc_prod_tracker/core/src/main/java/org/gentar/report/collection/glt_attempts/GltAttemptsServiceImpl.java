package org.gentar.report.collection.glt_attempts;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.gentar.report.Report;
import org.gentar.report.ReportService;
import org.gentar.report.collection.glt_attempts.projection.GltAttemptProjection;
import org.gentar.report.collection.glt_attempts.repository.GltAttemptRepository;
import org.springframework.stereotype.Component;

@Component
public class GltAttemptsServiceImpl implements GltAttemptsService {

    private final ReportService reportService;
    private final GltAttemptRepository gltAttemptRepository;

    public GltAttemptsServiceImpl(ReportService reportService,
                                  GltAttemptRepository gltAttemptRepository) {
        this.reportService = reportService;
        this.gltAttemptRepository = gltAttemptRepository;
    }

    @Override
    public void generateGltAttemptsReport(HttpServletResponse response,
                                          String reportType,
                                          String attempt,
                                          String workUnit,
                                          String workGroup,
                                          String startYear,
                                          String endYear,
                                          String startMonth,
                                          String endMonth)
        throws IOException, ParseException {


        if (workUnit != null) {
            if (workGroup != null) {
                gltAttemptsByAttemptWithWorkUnitWorkGroup(response, reportType, attempt, workUnit,
                    workGroup, startYear,
                    endYear, startMonth,
                    endMonth);
            } else {
                gltAttemptsByAttemptWithWorkUnit(response, reportType, attempt, workUnit, startYear,
                    endYear, startMonth,
                    endMonth);
            }
        } else {
            gltAttemptsByAttemptWithoutWorkUnit(response, reportType, attempt, startYear, endYear,
                startMonth,
                endMonth);
        }

    }

    private void gltAttemptsByAttemptWithWorkUnit(HttpServletResponse response,
                                                  String reportType,
                                                  String attempt,
                                                  String workUnit,
                                                  String startYear,
                                                  String endYear,
                                                  String startMonth,
                                                  String endMonth)
        throws IOException, ParseException {
        if ("year".equals(reportType)) {
            gltAttemptsByAttemptWithWorkUnitForYear(response, reportType, attempt, workUnit,
                startYear, endYear);

        } else if ("month".equals(reportType)) {
            gltAttemptsByAttemptWithWorkUnitForMonth(response, reportType, attempt, workUnit,
                startYear, endYear, startMonth,
                endMonth);
        }
    }

    private void gltAttemptsByAttemptWithWorkUnitForYear(HttpServletResponse response,
                                                         String reportType,
                                                         String attempt,
                                                         String workUnit,
                                                         String startYear,
                                                         String endYear)
        throws IOException, ParseException {
        List<GltAttemptProjection> gltAttemptProjections;


        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        gltAttemptProjections = gltAttemptRepository
            .findGltAttemptsByAttemptTypeByWorkUnitWithYear(attempt, workUnit, startTimestamp,
                endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptProjections, "workUnitYear");
    }

    private void gltAttemptsByAttemptWithWorkUnitForMonth(HttpServletResponse response,
                                                          String reportType,
                                                          String attempt,
                                                          String workUnit,
                                                          String startYear,
                                                          String endYear,
                                                          String startMonth,
                                                          String endMonth)
        throws IOException, ParseException {
        List<GltAttemptProjection> gltAttemptProjections;

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        gltAttemptProjections =
            gltAttemptRepository
                .findGltAttemptsByAttemptTypeByWorkUnitWithMonth(attempt, workUnit, startTimestamp,
                    endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptProjections, "workUnitYearMonth");
    }

    private void gltAttemptsByAttemptWithWorkUnitWorkGroup(HttpServletResponse response,
                                                           String reportType,
                                                           String attempt,
                                                           String workUnit,
                                                           String workGroup,
                                                           String startYear,
                                                           String endYear,
                                                           String startMonth,
                                                           String endMonth)
        throws IOException, ParseException {
        if ("year".equals(reportType)) {
            gltAttemptsByAttemptWithWorkUnitWorkGroupForYear(response, reportType, attempt,
                workUnit, workGroup,
                startYear, endYear);

        } else if ("month".equals(reportType)) {
            gltAttemptsByAttemptWithWorkUnitWorkGroupForMonth(response, reportType, attempt,
                workUnit, workGroup,
                startYear, endYear, startMonth,
                endMonth);
        }
    }

    private void gltAttemptsByAttemptWithWorkUnitWorkGroupForYear(HttpServletResponse response,
                                                                  String reportType,
                                                                  String attempt,
                                                                  String workUnit,
                                                                  String workGroup,
                                                                  String startYear,
                                                                  String endYear)
        throws IOException, ParseException {
        List<GltAttemptProjection> gltAttemptProjections;


        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        gltAttemptProjections = gltAttemptRepository
            .findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithYear(attempt, workUnit, workGroup,
                startTimestamp,
                endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptProjections, "workUnitWorkGroupYear");
    }

    private void gltAttemptsByAttemptWithWorkUnitWorkGroupForMonth(HttpServletResponse response,
                                                                   String reportType,
                                                                   String attempt,
                                                                   String workUnit,
                                                                   String workGroup,
                                                                   String startYear,
                                                                   String endYear,
                                                                   String startMonth,
                                                                   String endMonth)
        throws IOException, ParseException {
        List<GltAttemptProjection> gltAttemptProjections;

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        gltAttemptProjections =
            gltAttemptRepository
                .findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithMonth(attempt, workUnit,
                    workGroup, startTimestamp,
                    endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptProjections, "workUnitWorkGroupYearMonth");
    }

    private void gltAttemptsByAttemptWithoutWorkUnit(HttpServletResponse response,
                                                     String reportType,
                                                     String attempt,
                                                     String startYear,
                                                     String endYear,
                                                     String startMonth,
                                                     String endMonth)
        throws IOException, ParseException {

        List<GltAttemptProjection>
            gltAttemptProjectionsResult = new ArrayList<>();

        if ("year".equals(reportType)) {

            gltAttemptsByAttemptWithoutWorkUnitForYear(response, reportType, attempt, startYear,
                endYear,
                gltAttemptProjectionsResult);

        } else if ("month".equals(reportType)) {
            gltAttemptsByAttemptWithoutWorkUnitForMonth(response, reportType, attempt, startYear,
                endYear, startMonth, endMonth,
                gltAttemptProjectionsResult);
        }
    }


    private void gltAttemptsByAttemptWithoutWorkUnitForYear(HttpServletResponse response,
                                                            String reportType,
                                                            String attempt,
                                                            String startYear,
                                                            String endYear,
                                                            List<GltAttemptProjection> gltAttemptProjectionsResult)
        throws IOException, ParseException {


        List<GltAttemptProjection> gltAttemptProjections;

        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        gltAttemptProjections = gltAttemptRepository
            .findGltAttemptsByAttemptTypeWithYear(attempt,
                startTimestamp,
                endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptProjections, "Year");

    }

    private void gltAttemptsByAttemptWithoutWorkUnitForMonth(HttpServletResponse response,
                                                             String reportType,
                                                             String attempt,
                                                             String startYear,
                                                             String endYear,
                                                             String startMonth,
                                                             String endMonth,
                                                             List<GltAttemptProjection> gltAttemptProjectionsResult)
        throws IOException, ParseException {


        List<GltAttemptProjection> gltAttemptProjections;

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        gltAttemptProjections = gltAttemptRepository
            .findGltAttemptsByAttemptTypeWithMonth(attempt,
                startTimestamp,
                endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptProjections, "YearMonth");

    }

    private void formatProjectionReportText(HttpServletResponse response,
                                            String reportType,
                                            String attempt,
                                            List<GltAttemptProjection> gltAttemptProjections,
                                            String tsvType)
        throws IOException {
        StringBuilder reportText = new StringBuilder();
        if (tsvType.equals("workUnitYearMonth")) {
            reportText.append("Work Unit\tYear\tMonth\tSum\n");
        } else if (tsvType.equals("workUnitYear")) {
            reportText.append("Work Unit\tYear\tSum\n");
        } else if (tsvType.equals("workUnitWorkGroupYearMonth")) {
            reportText.append("Work Unit\tWork Group\tYear\tMonth\tSum\n");
        } else if (tsvType.equals("workUnitWorkGroupYear")) {
            reportText.append("Work Unit\tWork Group\tYear\tSum\n");
        } else if (tsvType.equals("YearMonth")) {
            reportText.append("Year\tMonth\tSum\n");
        } else if (tsvType.equals("Year")) {
            reportText.append("Year\tSum\n");
        }


        for (GltAttemptProjection gltAttemptProjection : gltAttemptProjections) {
            if (tsvType.equals("workUnitYearMonth") || tsvType.equals("workUnitYear") ||
                tsvType.equals("workUnitWorkGroupYearMonth") ||
                tsvType.equals("workUnitWorkGroupYear")) {
                reportText.append(gltAttemptProjection.getWorkUnitName()).append("\t");

                if (tsvType.equals("workUnitWorkGroupYearMonth") ||
                    tsvType.equals("workUnitWorkGroupYear")) {
                    reportText.append(gltAttemptProjection.getWorkGroupName()).append("\t");
                    if (tsvType.equals("workUnitWorkGroupYearMonth")) {
                        reportText.append(gltAttemptProjection.getMonth())
                            .append("\t");
                    }
                }
            }


            reportText.append(gltAttemptProjection.getYear());
            reportText.append("\t");

            if (tsvType.equals("YearMonth") || tsvType.equals("workUnitYearMonth")) {
                reportText.append(gltAttemptProjection.getMonth())
                    .append("\t");
            }
            reportText.append(gltAttemptProjection.getSum());
            reportText.append("\n");

        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
            .writeReport(response, "GLT_Production_Number_Report",
                report);
    }


    private Timestamp getEndDate(String endYear,
                                 String endMonth)
        throws ParseException {
        String endDateString = "";
        if (endYear == null || endYear.equals("")) {
            endDateString = "3000-12-31";
        } else {
            if (endMonth == null || endMonth.equals("")) {
                endDateString = endYear + "-12-31";
            } else {
                endDateString = endYear + "-" + endMonth + "-31";
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = dateFormat.parse(endDateString);
        return new Timestamp(endDate.getTime());
    }

    private Timestamp getStartDate(String startYear,
                                   String startMonth)
        throws ParseException {
        String startDateString = "";
        if (startYear == null || startYear.equals("")) {
            startDateString = "1000-01-01";
        } else {

            if (startMonth == null || startMonth.equals("")) {
                startDateString = startYear + "-01-01";
            } else {
                startDateString = startYear + "-" + startMonth + "-01";
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse(startDateString);
        return new Timestamp(startDate.getTime());
    }

}
