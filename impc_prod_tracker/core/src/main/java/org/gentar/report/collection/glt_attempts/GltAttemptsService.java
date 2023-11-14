package org.gentar.report.collection.glt_attempts;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import org.gentar.report.collection.glt_attempts.projection.GltAttemptsProjection;

public interface GltAttemptsService {

    void generateGltAttemptsReport(HttpServletResponse response,
                                   String reportType,
                                   String attempt,
                                   List<String> workUnit,
                                   String workGroup,
                                   String startYear,
                                   String endYear,
                                   String startMonth,
                                   String endMonth)
        throws IOException, ParseException;

    List<GltAttemptsProjection>  generateGltAttemptsJson(
        String reportType,
        String attempt,
        List<String> workUnit,
        String workGroup,
        String startYear,
        String endYear,
        String startMonth,
        String endMonth)
        throws ParseException, IOException;


    void generateGltAttemptsIntersectionReport(HttpServletResponse response) throws IOException;

    List<GltAttemptsProjection> getGltAttemptsIntersectionJson();

    void generateGltAttemptsUnionReport(HttpServletResponse response) throws IOException;

    List<GltAttemptsProjection> getGltAttemptsUnionJson();




}
