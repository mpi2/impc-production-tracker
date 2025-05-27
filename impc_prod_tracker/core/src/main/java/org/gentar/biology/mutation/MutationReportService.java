package org.gentar.biology.mutation;

import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.mutation.genome_browser.SerializedGuideProjection;

import java.io.IOException;
import java.util.List;

public interface MutationReportService {

    void getDeletionCoordinates(HttpServletResponse response) throws IOException;

    void getTargetedExons(HttpServletResponse response) throws IOException;

    void getCanonicalTargetedExons(HttpServletResponse response) throws IOException;

    void getGenomeBrowserCombine(HttpServletResponse response, String workUnit) throws IOException;

    List<SerializedGuideProjection> getSerializedGuides(HttpServletResponse response) throws IOException;

}
