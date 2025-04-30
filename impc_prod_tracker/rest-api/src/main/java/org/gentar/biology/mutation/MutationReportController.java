package org.gentar.biology.mutation;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports/mutation")
public class MutationReportController {

    private final MutationReportService mutationReportService;

    public MutationReportController(MutationReportService mutationReportService) {
        this.mutationReportService = mutationReportService;
    }

    @GetMapping(value = {"/deletion_coordinates"})
    public void getDeletionCoordinates(HttpServletResponse response) throws IOException {
        mutationReportService.getDeletionCoordinates(response);
    }

    @GetMapping(value = {"/targeted_exons"})
    public void getTargetedExons(HttpServletResponse response) throws IOException {

        mutationReportService.getTargetedExons(response);
    }

    @GetMapping(value = {"/canonical_targeted_exons"})
    public void getCanonicalTargetedExons(HttpServletResponse response) throws IOException {
        mutationReportService.getCanonicalTargetedExons(response);
    }
}


