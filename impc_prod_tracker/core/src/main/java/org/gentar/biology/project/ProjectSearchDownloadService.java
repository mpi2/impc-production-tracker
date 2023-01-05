package org.gentar.biology.project;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.project.search.filter.ProjectFilter;

/**
 * DownloadService.
 */
public interface ProjectSearchDownloadService {

    void writeReport(HttpServletResponse response, ProjectFilter projectFilter) throws IOException;

    void writeReportCaches();
}
