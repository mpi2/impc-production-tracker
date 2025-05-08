package org.gentar.biology.mutation;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MutationReportService {

    void getDeletionCoordinates(HttpServletResponse response) throws IOException;

    void getTargetedExons(HttpServletResponse response) throws IOException;

    void getCanonicalTargetedExons(HttpServletResponse response) throws IOException;

    void getGenomeBrowserCombine(HttpServletResponse response) throws IOException;

    }
