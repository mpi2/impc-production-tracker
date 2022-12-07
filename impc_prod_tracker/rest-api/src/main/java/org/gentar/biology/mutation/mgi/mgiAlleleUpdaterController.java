package org.gentar.biology.mutation.mgi;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.gentar.biology.mutation.MutationServiceImpl;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracking-api")
public class mgiAlleleUpdaterController {
    private final MutationServiceImpl mutationService;

    public mgiAlleleUpdaterController(
        MutationServiceImpl mutationService) {
        this.mutationService = mutationService;
    }

    // http://localhost:8080/tracking-api/mgiAllele
    @PutMapping(value = {"/mgiAllele"})
    public MgiAlleleResponseDto update(HttpServletResponse response, @RequestBody List<MgiAlleleDto> mgiAlleleDto) {
        return mutationService.updateMutationMgiAlleleId(mgiAlleleDto);
    }

}
