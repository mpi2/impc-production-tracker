package org.gentar.biology.targ_rep.targeting_vector;

import org.gentar.biology.targ_rep.TargRepTargetingVectorResponseDTO;
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
 * TargRepTargetingVectorController.
 */
@RestController
@RequestMapping("/api/targ_rep")
@CrossOrigin(origins = "*")
public class TargRepTargetingVectorController {

    private final TargRepTargetingVectorResponseMapper targetingVectorResponseMapper;
    private final TargRepTargetingVectorService targetingVectorService;


    public TargRepTargetingVectorController(
        TargRepTargetingVectorResponseMapper targetingVectorResponseMapper,
        TargRepTargetingVectorService targetingVectorService) {
        this.targetingVectorResponseMapper = targetingVectorResponseMapper;
        this.targetingVectorService = targetingVectorService;
    }


    @GetMapping(value = {"/targeting_vectors"})
    public ResponseEntity findAllTargRepTargetingVector(
        final Pageable targetingVectorPageable,
        final PagedResourcesAssembler targetingVectorAssembler) {

        Page<TargRepTargetingVector> targetingVectors = targetingVectorService.getPageableTargRepTargetingVector(targetingVectorPageable);
        Page<TargRepTargetingVectorResponseDTO> targRepTargetingVectorResponseDTOS =
            targetingVectors.map(targetingVectorResponseMapper::toDto);

        Link link = linkTo(methodOn(TargRepTargetingVectorController.class)
            .findAllTargRepTargetingVector(targetingVectorPageable, targetingVectorAssembler))
            .withSelfRel();
        link = link.withHref(decode(link.getHref()));
        PagedModel pr =
            targetingVectorAssembler.toModel(targRepTargetingVectorResponseDTOS,
                link);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }


    @GetMapping(value = {"/targeting_vectors/{id}"})
    public EntityModel<TargRepTargetingVectorResponseDTO> findTargRepTargetingVectorById(@PathVariable Long id) {
        EntityModel<TargRepTargetingVectorResponseDTO> entityModel;
        TargRepTargetingVector targRepTargetingVector = targetingVectorService.getNotNullTargRepTargetingVectorById(id);
        entityModel = EntityModel.of(targetingVectorResponseMapper.toDto(targRepTargetingVector));
        return entityModel;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
