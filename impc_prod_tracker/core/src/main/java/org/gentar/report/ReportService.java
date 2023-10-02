package org.gentar.report;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.gentar.report.dto.crispr_product.CrisprProductDTO;

public interface ReportService {

    void saveReport(ReportTypeName reportTypeName, String reportText);
    void cleanAllReports();
    void cleanReportsByReportType(String name);
    void writeReport(HttpServletResponse response, String name)throws IOException;
    void writeReport(HttpServletResponse response, String name, Report report) throws IOException;

    String getReportAsJson(String name);

    List<CrisprProductDTO> getReportsAsDtoList(String crisprProductData);
}
