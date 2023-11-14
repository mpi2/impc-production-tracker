package org.gentar.report.collection.glt_attempts;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.gentar.report.Report;
import org.gentar.report.ReportService;
import org.gentar.report.collection.glt_attempts.projection.GltAttemptsProjection;
import org.gentar.report.collection.glt_attempts.repository.GltAttemptsRepository;
import org.springframework.stereotype.Component;

@Component
public class GltAttemptsServiceImpl implements GltAttemptsService {

    private final ReportService reportService;
    private final GltAttemptsRepository gltAttemptsRepository;

    public GltAttemptsServiceImpl(ReportService reportService,
                                  GltAttemptsRepository gltAttemptsRepository) {
        this.reportService = reportService;
        this.gltAttemptsRepository = gltAttemptsRepository;
    }

    @Override
    public void generateGltAttemptsReport(HttpServletResponse response,
                                          String reportType,
                                          String attempt,
                                          List<String> workUnit,
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


    @Override
    public List<GltAttemptsProjection> generateGltAttemptsJson(
        String reportType,
        String attempt,
        List<String> workUnit,
        String workGroup,
        String startYear,
        String endYear,
        String startMonth,
        String endMonth)
        throws ParseException, IOException {


        if (workUnit != null) {
            if (workGroup != null) {
                return gltAttemptsByAttemptWithWorkUnitWorkGroupJson(reportType, attempt, workUnit,
                    workGroup, startYear,
                    endYear, startMonth,
                    endMonth);
            } else {
                return gltAttemptsByAttemptWithWorkUnitJson(reportType, attempt, workUnit,
                    startYear,
                    endYear, startMonth,
                    endMonth);
            }
        } else {
            return gltAttemptsByAttemptWithoutWorkUnitJson(reportType, attempt, startYear, endYear,
                startMonth,
                endMonth);
        }
    }

    @Override
    public void generateGltAttemptsIntersectionReport(HttpServletResponse response)
        throws IOException {

        List<GltAttemptsProjection> gltAttemptsProjections;

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsIntersection();

        formatProjectionIntersectionReportText(response, gltAttemptsProjections);
    }

    @Override
    public List<GltAttemptsProjection> getGltAttemptsIntersectionJson() {

        List<GltAttemptsProjection> gltAttemptsProjections;

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsIntersection();

        return gltAttemptsProjections;
    }

    @Override
    public void generateGltAttemptsUnionReport(HttpServletResponse response) throws IOException {
        List<GltAttemptsProjection> gltAttemptsProjections;

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsUnion();

        formatProjectionUnionReportText(response, gltAttemptsProjections);
    }

    @Override
    public List<GltAttemptsProjection> getGltAttemptsUnionJson() {
        List<GltAttemptsProjection> gltAttemptsProjections;

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsUnion();

        return gltAttemptsProjections;
    }

    private void gltAttemptsByAttemptWithWorkUnit(HttpServletResponse response,
                                                  String reportType,
                                                  String attempt,
                                                  List<String> workUnit,
                                                  String startYear,
                                                  String endYear,
                                                  String startMonth,
                                                  String endMonth)
        throws IOException, ParseException {
        if ("year".equalsIgnoreCase(reportType)) {
            gltAttemptsByAttemptWithWorkUnitForYear(response, reportType, attempt, workUnit,
                startYear, endYear);

        } else if ("month".equalsIgnoreCase(reportType)) {
            gltAttemptsByAttemptWithWorkUnitForMonth(response, reportType, attempt, workUnit,
                startYear, endYear, startMonth,
                endMonth);
        }
    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithWorkUnitJson(String reportType,
                                                                             String attempt,
                                                                             List<String> workUnit,
                                                                             String startYear,
                                                                             String endYear,
                                                                             String startMonth,
                                                                             String endMonth)
        throws ParseException {
        if ("year".equalsIgnoreCase(reportType)) {
            return gltAttemptsByAttemptWithWorkUnitForYearJson(attempt, workUnit,
                startYear, endYear);

        } else if ("month".equalsIgnoreCase(reportType)) {
            return gltAttemptsByAttemptWithWorkUnitForMonthJson(attempt, workUnit,
                startYear, endYear, startMonth,
                endMonth);
        }

        return null;
    }

    private void gltAttemptsByAttemptWithWorkUnitForYear(HttpServletResponse response,
                                                         String reportType,
                                                         String attempt,
                                                         List<String> workUnit,
                                                         String startYear,
                                                         String endYear)
        throws IOException, ParseException {
        List<GltAttemptsProjection> gltAttemptsProjections;


        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsByAttemptTypeByWorkUnitWithYear(attempt, workUnit, startTimestamp,
                endTimestamp);

        if (workUnit.size() > 1) {
            gltAttemptsProjections =
                formatGltAttemptsProjectionsForMultipleWorkunitForYear(gltAttemptsProjections);
        }

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptsProjections, "workUnitYear");
    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithWorkUnitForYearJson(
        String attempt,
        List<String> workUnit,
        String startYear,
        String endYear)
        throws ParseException {


        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        List<GltAttemptsProjection> gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsByAttemptTypeByWorkUnitWithYear(attempt, workUnit, startTimestamp,
                endTimestamp);

        if (workUnit.size() > 1) {
            return formatGltAttemptsProjectionsForMultipleWorkunitForYear(gltAttemptsProjections);
        }
        return gltAttemptsProjections;

    }

    private void gltAttemptsByAttemptWithWorkUnitForMonth(HttpServletResponse response,
                                                          String reportType,
                                                          String attempt,
                                                          List<String> workUnit,
                                                          String startYear,
                                                          String endYear,
                                                          String startMonth,
                                                          String endMonth)
        throws IOException, ParseException {
        List<GltAttemptsProjection> gltAttemptsProjections;

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        gltAttemptsProjections =
            gltAttemptsRepository
                .findGltAttemptsByAttemptTypeByWorkUnitWithMonth(attempt, workUnit, startTimestamp,
                    endTimestamp);

        if(workUnit.size()>1) {
            gltAttemptsProjections =
                formatGltAttemptsProjectionsForMultipleWorkunitForMonth(gltAttemptsProjections);
        }
        formatProjectionReportText(response, reportType, attempt,
            gltAttemptsProjections, "workUnitYearMonth");
    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithWorkUnitForMonthJson(
        String attempt,
        List<String> workUnit,
        String startYear,
        String endYear,
        String startMonth,
        String endMonth)
        throws ParseException {
        List<GltAttemptsProjection> gltAttemptsProjections;

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        gltAttemptsProjections =  gltAttemptsRepository
            .findGltAttemptsByAttemptTypeByWorkUnitWithMonth(attempt, workUnit, startTimestamp,
                endTimestamp);

        if(workUnit.size()>1) {
          return
                formatGltAttemptsProjectionsForMultipleWorkunitForMonth(gltAttemptsProjections);
        }

        return gltAttemptsProjections;

    }

    private void gltAttemptsByAttemptWithWorkUnitWorkGroup(HttpServletResponse response,
                                                           String reportType,
                                                           String attempt,
                                                           List<String> workUnit,
                                                           String workGroup,
                                                           String startYear,
                                                           String endYear,
                                                           String startMonth,
                                                           String endMonth)
        throws IOException, ParseException {

        if ("year".equalsIgnoreCase(reportType)) {
            gltAttemptsByAttemptWithWorkUnitWorkGroupForYear(response, reportType, attempt,
                workUnit, workGroup,
                startYear, endYear);

        } else if ("month".equalsIgnoreCase(reportType)) {
            gltAttemptsByAttemptWithWorkUnitWorkGroupForMonth(response, reportType, attempt,
                workUnit, workGroup,
                startYear, endYear, startMonth,
                endMonth);
        }
    }


    private List<GltAttemptsProjection> gltAttemptsByAttemptWithWorkUnitWorkGroupJson(
        String reportType,
        String attempt,
        List<String> workUnit,
        String workGroup,
        String startYear,
        String endYear,
        String startMonth,
        String endMonth)
        throws IOException, ParseException {

        if ("year".equalsIgnoreCase(reportType)) {
            return gltAttemptsByAttemptWithWorkUnitWorkGroupForYearJson(attempt,
                workUnit, workGroup,
                startYear, endYear);

        } else if ("month".equalsIgnoreCase(reportType)) {
            return gltAttemptsByAttemptWithWorkUnitWorkGroupForMonthJson(attempt,
                workUnit, workGroup,
                startYear, endYear, startMonth,
                endMonth);
        }

        return null;
    }

    private void gltAttemptsByAttemptWithWorkUnitWorkGroupForYear(HttpServletResponse response,
                                                                  String reportType,
                                                                  String attempt,
                                                                  List<String> workUnit,
                                                                  String workGroup,
                                                                  String startYear,
                                                                  String endYear)
        throws IOException, ParseException {
        List<GltAttemptsProjection> gltAttemptsProjections;


        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithYear(attempt, workUnit, workGroup,
                startTimestamp,
                endTimestamp);

        if (workUnit.size() > 1) {

        }

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptsProjections, "workUnitWorkGroupYear");
    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithWorkUnitWorkGroupForYearJson(
        String attempt,
        List<String> workUnit,
        String workGroup,
        String startYear,
        String endYear)
        throws ParseException {


        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        return gltAttemptsRepository
            .findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithYear(attempt, workUnit, workGroup,
                startTimestamp,
                endTimestamp);

    }

    private void gltAttemptsByAttemptWithWorkUnitWorkGroupForMonth(HttpServletResponse response,
                                                                   String reportType,
                                                                   String attempt,
                                                                   List<String> workUnit,
                                                                   String workGroup,
                                                                   String startYear,
                                                                   String endYear,
                                                                   String startMonth,
                                                                   String endMonth)
        throws IOException, ParseException {
        List<GltAttemptsProjection> gltAttemptsProjections;

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        gltAttemptsProjections =
            gltAttemptsRepository
                .findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithMonth(attempt, workUnit,
                    workGroup, startTimestamp,
                    endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptsProjections, "workUnitWorkGroupYearMonth");
    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithWorkUnitWorkGroupForMonthJson(
        String attempt,
        List<String> workUnit,
        String workGroup,
        String startYear,
        String endYear,
        String startMonth,
        String endMonth)
        throws ParseException {

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        return
            gltAttemptsRepository
                .findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithMonth(attempt, workUnit,
                    workGroup, startTimestamp,
                    endTimestamp);

    }

    private void gltAttemptsByAttemptWithoutWorkUnit(HttpServletResponse response,
                                                     String reportType,
                                                     String attempt,
                                                     String startYear,
                                                     String endYear,
                                                     String startMonth,
                                                     String endMonth)
        throws IOException, ParseException {

        List<GltAttemptsProjection>
            gltAttemptsProjectionsResult = new ArrayList<>();

        if ("year".equalsIgnoreCase(reportType)) {

            gltAttemptsByAttemptWithoutWorkUnitForYear(response, reportType, attempt, startYear,
                endYear,
                gltAttemptsProjectionsResult);

        } else if ("month".equalsIgnoreCase(reportType)) {
            gltAttemptsByAttemptWithoutWorkUnitForMonth(response, reportType, attempt, startYear,
                endYear, startMonth, endMonth,
                gltAttemptsProjectionsResult);
        }
    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithoutWorkUnitJson(String reportType,
                                                                                String attempt,
                                                                                String startYear,
                                                                                String endYear,
                                                                                String startMonth,
                                                                                String endMonth)
        throws IOException, ParseException {

        List<GltAttemptsProjection>
            gltAttemptsProjectionsResult = new ArrayList<>();

        if ("year".equalsIgnoreCase(reportType)) {

            return gltAttemptsByAttemptWithoutWorkUnitForYearJson(reportType, attempt, startYear,
                endYear,
                gltAttemptsProjectionsResult);

        } else if ("month".equalsIgnoreCase(reportType)) {
            return gltAttemptsByAttemptWithoutWorkUnitForMonthJson(reportType, attempt, startYear,
                endYear, startMonth, endMonth,
                gltAttemptsProjectionsResult);
        }

        return null;
    }


    private void gltAttemptsByAttemptWithoutWorkUnitForYear(HttpServletResponse response,
                                                            String reportType,
                                                            String attempt,
                                                            String startYear,
                                                            String endYear,
                                                            List<GltAttemptsProjection> gltAttemptsProjectionsResult)
        throws IOException, ParseException {


        List<GltAttemptsProjection> gltAttemptsProjections;

        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsByAttemptTypeWithYear(attempt,
                startTimestamp,
                endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptsProjections, "Year");

    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithoutWorkUnitForYearJson(
        String reportType,
        String attempt,
        String startYear,
        String endYear,
        List<GltAttemptsProjection> gltAttemptsProjectionsResult)
        throws IOException, ParseException {


        Timestamp startTimestamp = getStartDate(startYear, "");
        Timestamp endTimestamp = getEndDate(endYear, "");

        return gltAttemptsRepository
            .findGltAttemptsByAttemptTypeWithYear(attempt,
                startTimestamp,
                endTimestamp);


    }

    private void gltAttemptsByAttemptWithoutWorkUnitForMonth(HttpServletResponse response,
                                                             String reportType,
                                                             String attempt,
                                                             String startYear,
                                                             String endYear,
                                                             String startMonth,
                                                             String endMonth,
                                                             List<GltAttemptsProjection> gltAttemptsProjectionsResult)
        throws IOException, ParseException {


        List<GltAttemptsProjection> gltAttemptsProjections;

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        gltAttemptsProjections = gltAttemptsRepository
            .findGltAttemptsByAttemptTypeWithMonth(attempt,
                startTimestamp,
                endTimestamp);

        formatProjectionReportText(response, reportType, attempt,
            gltAttemptsProjections, "YearMonth");

    }

    private List<GltAttemptsProjection> gltAttemptsByAttemptWithoutWorkUnitForMonthJson(
        String reportType,
        String attempt,
        String startYear,
        String endYear,
        String startMonth,
        String endMonth,
        List<GltAttemptsProjection> gltAttemptsProjectionsResult)
        throws IOException, ParseException {

        Timestamp startTimestamp = getStartDate(startYear, startMonth);
        Timestamp endTimestamp = getEndDate(endYear, endMonth);

        return gltAttemptsRepository
            .findGltAttemptsByAttemptTypeWithMonth(attempt,
                startTimestamp,
                endTimestamp);

    }

    private void formatProjectionReportText(HttpServletResponse response,
                                            String reportType,
                                            String attempt,
                                            List<GltAttemptsProjection> gltAttemptsProjections,
                                            String tsvType)
        throws IOException {
        StringBuilder reportText = new StringBuilder();
        switch (tsvType) {
            case "workUnitYearMonth" -> reportText.append("Work Unit\tYear\tMonth\tSum\n");
            case "workUnitYear" -> reportText.append("Work Unit\tYear\tSum\n");
            case "workUnitWorkGroupYearMonth" -> reportText
                .append("Work Unit\tWork Group\tMonth\tYear\tSum\n");
            case "workUnitWorkGroupYear" -> reportText.append("Work Unit\tWork Group\tYear\tSum\n");
            case "YearMonth" -> reportText.append("Year\tMonth\tSum\n");
            case "Year" -> reportText.append("Year\tSum\n");
        }


        for (GltAttemptsProjection gltAttemptsProjection : gltAttemptsProjections) {
            if (tsvType.equals("workUnitYearMonth") || tsvType.equals("workUnitYear") ||
                tsvType.equals("workUnitWorkGroupYearMonth") ||
                tsvType.equals("workUnitWorkGroupYear")) {


                reportText.append(concatenateUniqueWorkUnits(gltAttemptsProjections)).append("\t");

                if (tsvType.equals("workUnitWorkGroupYearMonth") ||
                    tsvType.equals("workUnitWorkGroupYear")) {
                    reportText.append(gltAttemptsProjection.getWorkGroupName()).append("\t");
                    if (tsvType.equals("workUnitWorkGroupYearMonth")) {
                        reportText.append(gltAttemptsProjection.getMonth())
                            .append("\t");
                    }
                }
            }


            reportText.append(gltAttemptsProjection.getYear());
            reportText.append("\t");

            if (tsvType.equals("YearMonth") || tsvType.equals("workUnitYearMonth")) {
                reportText.append(gltAttemptsProjection.getMonth())
                    .append("\t");
            }
            reportText.append(gltAttemptsProjection.getSum());
            reportText.append("\n");

        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
            .writeReport(response, "Production_Numbers_" + reportType + "_" + attempt,
                report);
    }

    private void formatProjectionIntersectionReportText(HttpServletResponse response,
                                                        List<GltAttemptsProjection> gltAttemptsProjections)
        throws IOException {
        StringBuilder reportText = new StringBuilder();
        reportText.append("Symbol\n");


        for (GltAttemptsProjection gltAttemptsProjection : gltAttemptsProjections) {
            reportText.append(gltAttemptsProjection.getSymbol()).append("\n");

        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
            .writeReport(response, "Production_Numbers_Intersection",
                report);
    }

    private void formatProjectionUnionReportText(HttpServletResponse response,
                                                 List<GltAttemptsProjection> gltAttemptsProjections)
        throws IOException {
        StringBuilder reportText = new StringBuilder();
        reportText.append("count\n");


        for (GltAttemptsProjection gltAttemptsProjection : gltAttemptsProjections) {
            reportText.append(gltAttemptsProjection.getCount());

        }
        Report report = new Report();
        report.setReport(reportText.toString());
        report.setCreatedAt(LocalDateTime.now());
        reportService
            .writeReport(response, "Production_Numbers_Union",
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


    private List<GltAttemptsProjection> formatGltAttemptsProjectionsForMultipleWorkunitForYear(
        List<GltAttemptsProjection> gltAttemptsProjections) {

        Map<Long, GltAttemptsProjection> lastYearAttempts = new LinkedHashMap<>();

        for (GltAttemptsProjection gltAttemptsProjection : gltAttemptsProjections) {
            lastYearAttempts.put(gltAttemptsProjection.getYear(), gltAttemptsProjection);
        }

        return new ArrayList<>(lastYearAttempts.values());

    }

    private List<GltAttemptsProjection> formatGltAttemptsProjectionsForMultipleWorkunitForMonth(
        List<GltAttemptsProjection> gltAttemptsProjections) {

        Map<Long, Map<Long, GltAttemptsProjection>> yearMonthAttempts = new LinkedHashMap<>();

        for (GltAttemptsProjection gltAttemptsProjection : gltAttemptsProjections) {
            Long year = gltAttemptsProjection.getYear();
            Long month = gltAttemptsProjection.getMonth();

            yearMonthAttempts
                .computeIfAbsent(year, k -> new LinkedHashMap<>())
                .put(month, gltAttemptsProjection);
        }

        List<GltAttemptsProjection> filteredGltAttemptsProjection = new ArrayList<>();
        yearMonthAttempts.forEach((year, monthMap) -> filteredGltAttemptsProjection.addAll(monthMap.values()));

        return filteredGltAttemptsProjection;

    }


    private static String concatenateUniqueWorkUnits(List<GltAttemptsProjection> gltAttemptsProjections) {
        Set<String> uniqueWorkUnits = gltAttemptsProjections.stream()
            .map(GltAttemptsProjection::getWorkUnitName) // Replace "YourObject" with your actual class name
            .collect(Collectors.toSet());

        return String.join(" ", uniqueWorkUnits); // Join unique values with a space delimiter
    }
}