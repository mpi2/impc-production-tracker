package org.gentar.biology.targ_rep.genbank_file;

import org.gentar.biology.targ_rep.gene_bank_file.TargRepGenBankFileResponseDTO;
import org.gentar.helpers.LinkUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        Link link = linkTo(methodOn(TargRepGenBankFileController.class)
            .findAllTargRepGenBankFile(genBankFilePageable, genBankFileAssembler))
            .withSelfRel();
        link = link.withHref(decode(link.getHref()));
        PagedModel pr =
            genBankFileAssembler.toModel(targRepGenBankFileResponseDTOS,
                link);

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

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
