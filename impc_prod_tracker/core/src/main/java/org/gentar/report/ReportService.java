package org.gentar.report;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportService {

    void saveReport(ReportTypeName reportTypeName, String reportText);
    void cleanAllReports();
    void cleanReportsByReportType(String name);
    void writeReport(HttpServletResponse response, String name) throws IOException;
}
