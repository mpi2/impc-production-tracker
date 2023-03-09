package org.gentar.report.collection.glt_attempts;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface GltAttemptsService {

    void generateGltAttemptsReport(HttpServletResponse response,
                                   String reportType,
                                   String attempt,
                                   String workUnit,
                                   String workGroup,
                                   String startYear,
                                   String endYear,
                                   String startMonth,
                                   String endMonth)
        throws IOException, ParseException;


}
