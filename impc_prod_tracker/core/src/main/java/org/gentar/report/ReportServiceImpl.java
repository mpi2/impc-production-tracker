package org.gentar.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.gentar.BaseEntity;
import org.gentar.report.dto.crispr_product.CrisprProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
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
                             ReportTypeServiceImpl reportTypeService) {
        this.reportRepository = reportRepository;
        this.reportTypeRepository = reportTypeRepository;
        this.reportTypeService = reportTypeService;
    }

    @Transactional
    @Override
    public void saveReport(ReportTypeName reportTypeName, String reportText) {
        ReportType reportType =
            reportTypeRepository.findReportTypeByNameIs(reportTypeName.getLabel());
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
            if (!reportsToRemove.isEmpty()) {
                reportRepository.deleteAll(reportsToRemove);
            }
        }
    }

    @Override
    public void writeReport(HttpServletResponse response, String name) throws IOException {

        String reportTypeName = name.toLowerCase();

        if (reportTypeNameExists(reportTypeName)) {

            Report report =
                reportRepository.findFirstByReportType_NameIsOrderByCreatedAtDesc(reportTypeName);
            if (reportExists(report)) {
                printReport(response, reportTypeName, report);
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }


        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }


    @Override
    public void writeReport(HttpServletResponse response, String name, Report report)
        throws IOException {

        if (report != null && reportExists(report)) {
            printReport(response, name, report);
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }

    }

    @Override
    public String getReportAsJson(String name) {

        String reportTypeName = name.toLowerCase();

        if (reportTypeNameExists(reportTypeName)) {

            Report report =
                reportRepository.findFirstByReportType_NameIsOrderByCreatedAtDesc(reportTypeName);
            if (reportExists(report)) {
                return report.getReport();
            } else {
                return "report name does not exist";
            }

        } else {
            return "report does not exist";
        }
    }


    public List<CrisprProductDTO> getReportsAsDtoList(String reportType) {
        List<CrisprProductDTO> dtoList = new ArrayList<>();

       String crisprProductData =  getReportAsJson(reportType);

        String[] jsonObjects = crisprProductData.split("\n");

            for (String report : jsonObjects) {

                // Use ObjectMapper to deserialize the JSON into MutationDTO
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    CrisprProductDTO dto = objectMapper.readValue(report, CrisprProductDTO.class);
                    dtoList.add(dto);
                } catch (Exception e) {
                    // Handle any exceptions, e.g., JSON parsing errors
                    e.printStackTrace();
                    // You may want to continue processing other reports or handle the error differently
                }
            }

            return dtoList;
    }

    private void printReport(HttpServletResponse response, String reportName, Report report)
        throws IOException {

        PrintWriter output = response.getWriter();

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
//        response.setContentType("text/tab-separated-values; charset=utf-8");
//        response.setContentType("application/txt");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition",
            "attachment; filename=" + reportName + "_" + report.getCreatedAt() + ".tsv");

        String data = report.getReport();
        response.setContentLength(data.length());

        output.println(data);
        output.flush();
        output.close();
    }

    private Boolean reportExists(Report report) {
        if (report == null) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private Boolean reportTypeNameExists(String reportTypeName) {
        if (reportTypeService.reportTypeNameExists(reportTypeName) &&
            reportTypeService.reportTypeExistsInDatabase(reportTypeName)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}