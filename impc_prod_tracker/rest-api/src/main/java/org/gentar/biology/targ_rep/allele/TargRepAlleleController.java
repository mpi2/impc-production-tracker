package org.gentar.biology.targ_rep.allele;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.gentar.biology.targ_rep.TargRepAlleleResponseDTO;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellService;
import org.gentar.biology.targ_rep.mutation.TargRepEsCellMutation;
import org.gentar.biology.targ_rep.mutation.TargRepEsCellMutationService;
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
 * TargRepAlleleController.
 */
@RestController
@RequestMapping("/api/targ_rep")
@CrossOrigin(origins = "*")
public class TargRepAlleleController {

    private final TargRepAlleleResponseMapper targRepAlleleResponseMapper;
    private final TargRepAlleleService alleleService;




    public TargRepAlleleController(TargRepAlleleResponseMapper targRepAlleleResponseMapper,
                                   TargRepAlleleService alleleService) {
        this.targRepAlleleResponseMapper = targRepAlleleResponseMapper;
        this.alleleService = alleleService;
    }


    /**
     * Get the targ rep alleles.
     *
     * @return A collection  of {@link TargRepAlleleResponseDTO} objects.
     */
    @GetMapping(value = {"/alleles"})
    public ResponseEntity findAllTargRepAlleles(
        final Pageable allelePageable,
        final PagedResourcesAssembler alleleAssembler) {

        Page<TargRepAllele> targRepAlleles = alleleService.getPageableTargRepAllele(allelePageable);
        Page<TargRepAlleleResponseDTO> targRepAlleleResponseDtosPage =
            targRepAlleles.map(targRepAlleleResponseMapper::toDto);

        PagedModel pr =
            alleleAssembler.toModel(targRepAlleleResponseDtosPage,
                linkTo(methodOn(TargRepAlleleController.class)
                    .findAllTargRepAlleles(allelePageable, alleleAssembler)).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }


    /**
     * Get a specific targ rep allele.
     *
     * @param id TargRep Allele identifier.
     * @return Entity with the targ rep allele information.
     */
    @GetMapping(value = {"/alleles/{id}"})
    public EntityModel<TargRepAlleleResponseDTO> findTargRepAlleleById(@PathVariable Long id) {
        EntityModel<TargRepAlleleResponseDTO> entityModel;
        TargRepAllele targRepAllele = alleleService.getNotNullTargRepAlelleById(id);
        entityModel = EntityModel.of(targRepAlleleResponseMapper.toDto(targRepAllele));
        return entityModel;
    }


}
