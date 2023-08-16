package org.gentar.report.collection.work_unit_attempts;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface WorkUnitAttemptsService {

    void generateWorkUnitAttemptsReport(HttpServletResponse response,
                                   String workUnit,
                                   String attempt)
        throws IOException, ParseException;

}
