package org.gentar.report;

import org.gentar.BaseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportTypeServiceImpl reportTypeService;

    public ReportServiceImpl(ReportRepository reportRepository,
                             ReportTypeRepository reportTypeRepository,
                             ReportTypeServiceImpl reportTypeService)
    {
        this.reportRepository = reportRepository;
        this.reportTypeRepository = reportTypeRepository;
        this.reportTypeService = reportTypeService;
    }

    @Transactional
    @Override
    public void saveReport(ReportTypeName reportTypeName, String reportText)
    {
        ReportType reportType = reportTypeRepository.findReportTypeByNameIs(reportTypeName.getLabel());
        Report report = new Report();
        report.setReport(reportText);
        report.setReportType(reportType);
        reportRepository.save(report);
     }

    @Override
    @Transactional
    public void cleanAllReports() {
        ReportTypeName
                .stream()
                .forEach(rt -> cleanReportsByReportType(rt.getLabel()));
    }

    @Override
    @Transactional
    public void cleanReportsByReportType(String name) {

        if (reportTypeNameExists(name)) {
            List<Report> reportList = reportRepository.findAllByReportType_NameIs(name);
            List<Report> reportsToRemove = reportList
                    .stream()
                    .sorted(Comparator.comparing(BaseEntity::getCreatedAt).reversed())
                    .skip(2)
                    .collect(Collectors.toList());
            if (reportsToRemove.size() > 0) {
                reportRepository.deleteAll(reportsToRemove);
            }
        }
    }

    @Override
    public void writeReport(HttpServletResponse response, String name) throws IOException {

        String reportTypeName = name.toLowerCase();

        if (reportTypeNameExists(reportTypeName)) {

            Report report = reportRepository.findFirstByReportType_NameIsOrderByCreatedAtDesc(reportTypeName);
            if (reportExists(report)) {
                printReport(response, reportTypeName, report);
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }


        }else{
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }


    private void printReport(HttpServletResponse response, String reportName, Report report) throws IOException {

        PrintWriter output = response.getWriter();

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Content-disposition", "attachment; filename=" + reportName + "_" + report.getCreatedAt() + ".tsv");

        output.println(report.getReport());
        output.flush();
        output.close();
    }

    private Boolean reportExists(Report report) {
        if (report == null){
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private Boolean reportTypeNameExists(String reportTypeName){
        if (reportTypeService.reportTypeNameExists(reportTypeName) &&
                reportTypeService.reportTypeExistsInDatabase(reportTypeName)) {
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }
}
