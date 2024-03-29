package org.gentar.biology.mutation.mgi;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

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

    @PutMapping(value = {"/mutation/mgiAllele/update"})
    public MgiAlleleResponseDto update(HttpServletResponse response, @RequestBody List<MgiAlleleDto> mgiAlleleDto) {
        return mutationService.updateMutationMgiAlleleId(mgiAlleleDto);
    }

}
