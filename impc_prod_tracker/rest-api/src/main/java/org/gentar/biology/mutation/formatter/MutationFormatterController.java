package org.gentar.biology.mutation.formatter;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MutationFormatterController {
    private final MutationFormatterServiceImpl mutationFormatterService;

    public MutationFormatterController(
        MutationFormatterServiceImpl mutationFormatterService) {
        this.mutationFormatterService = mutationFormatterService;
    }

    // http://localhost:8080/api/format/sequence/UCD
    @PutMapping(value = {"/format/sequence/{workUnit}"})
    public void formatSequence(HttpServletResponse response, @PathVariable String workUnit) {
        mutationFormatterService.formatSequence(workUnit);
    }

}
