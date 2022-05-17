package org.gentar.biology.targ_rep.genbank_file;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.gentar.biology.targ_rep.TargRepGenBankFileResponseDTO;
import org.gentar.helpers.LinkUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TargRepGenBankFileController.
 */
@RestController
@RequestMapping("/api/targ_rep")
@CrossOrigin(origins = "*")
public class TargRepGenBankFileController {

    private final TargRepGenBankFileResponseMapper genBankFileResponseMapper;
    private final TargRepGenBankFileService genBankFileService;


    public TargRepGenBankFileController(
        TargRepGenBankFileResponseMapper genBankFileResponseMapper,
        TargRepGenBankFileService genBankFileService) {
        this.genBankFileResponseMapper = genBankFileResponseMapper;
        this.genBankFileService = genBankFileService;
    }


    @GetMapping(value = {"/genbank_file"})
    public ResponseEntity findAllTargRepGenBankFile(
        final Pageable genBankFilePageable,
        final PagedResourcesAssembler genBankFileAssembler) {

        Page<TargRepGenbankFile> genBankFiles =
            genBankFileService.getPageableTargRepGenBankFile(genBankFilePageable);
        Page<TargRepGenBankFileResponseDTO> targRepGenBankFileResponseDTOS =
            genBankFiles.map(genBankFileResponseMapper::toDto);

        PagedModel pr =
            genBankFileAssembler.toModel(targRepGenBankFileResponseDTOS,
                linkTo(methodOn(TargRepGenBankFileController.class)
                    .findAllTargRepGenBankFile(genBankFilePageable, genBankFileAssembler))
                    .withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }


    @GetMapping(value = {"/genbank_file/{id}"})
    public EntityModel<TargRepGenBankFileResponseDTO> findTargRepGenBankFileById(
        @PathVariable Long id) {
        EntityModel<TargRepGenBankFileResponseDTO> entityModel;
        TargRepGenbankFile targRepGenBankFile =
            genBankFileService.getNotNullTargRepGenBankFileById(id);
        entityModel = EntityModel.of(genBankFileResponseMapper.toDto(targRepGenBankFile));
        return entityModel;
    }


}
