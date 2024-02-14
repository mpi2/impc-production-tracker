package org.gentar.biology.targ_rep.pipeline;

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
 * TargRepPipelineController.
 */
@RestController
@RequestMapping("/api/targ_rep")
@CrossOrigin(origins = "*")
public class TargRepPipelineController {
    private final TargRepPipelineResponseMapper targRepPipelineResponseMapper;
    private final TargRepPipelineService targRepPipelineService;

    public TargRepPipelineController(TargRepPipelineResponseMapper targRepPipelineResponseMapper,
                                     TargRepPipelineServiceImpl targRepPipelineService) {
        this.targRepPipelineResponseMapper = targRepPipelineResponseMapper;
        this.targRepPipelineService = targRepPipelineService;
    }


    /**
     * Get the targ rep pipelines.
     *
     * @return A collection  of {@link TargRepPipelineResponseDTO} objects.
     */
    @GetMapping(value = {"/pipelines"})
    public ResponseEntity findAllTargRepPipelines(
        final Pageable pageable,
        final PagedResourcesAssembler assembler) {

        Page<TargRepPipeline> targRepPipelines =
            targRepPipelineService.getPageableTargRepPipeline(pageable);
        Page<TargRepPipelineResponseDTO> targRepPipelineResponseDtosPage =
            targRepPipelines.map(this::getDto);

        Link link = linkTo(methodOn(TargRepPipelineController.class)
            .findAllTargRepPipelines(pageable, assembler)).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        PagedModel pr =
            assembler.toModel(targRepPipelineResponseDtosPage,
                link);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private TargRepPipelineResponseDTO getDto(TargRepPipeline targRepPipeline) {
        TargRepPipelineResponseDTO targRepPipelineResponseDto = new TargRepPipelineResponseDTO();
        if (targRepPipeline != null) {
            targRepPipelineResponseDto = targRepPipelineResponseMapper.toDto(targRepPipeline);
        }
        return targRepPipelineResponseDto;
    }


    /**
     * Get a specific targ rep pipeline.
     *
     * @param id Pipeline identifier.
     * @return Entity with the targ rep pipeline information.
     */
    @GetMapping(value = {"/pipelines/{id}"})
    public EntityModel<TargRepPipelineResponseDTO> findTargRepPipelineById(@PathVariable Long id) {
        EntityModel<TargRepPipelineResponseDTO> entityModel;
        TargRepPipeline targRepPipeline = targRepPipelineService.getNotNullTargRepPipelineById(id);
        entityModel = EntityModel.of(targRepPipelineResponseMapper.toDto(targRepPipeline));
        return entityModel;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

}
