package org.gentar.report.collection.glt_attempts;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import org.gentar.report.collection.glt_attempts.projection.GltAttemptProjection;

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

    List<GltAttemptProjection>  generateGltAttemptsJson(
        String reportType,
        String attempt,
        String workUnit,
        String workGroup,
        String startYear,
        String endYear,
        String startMonth,
        String endMonth)
        throws ParseException, IOException;


    void generateGltAttemptsIntersectionReport(HttpServletResponse response) throws IOException;

    List<GltAttemptProjection> getGltAttemptsIntersectionJson();

    void generateGltAttemptsUnionReport(HttpServletResponse response) throws IOException;

    List<GltAttemptProjection> getGltAttemptsUnionJson();




}
