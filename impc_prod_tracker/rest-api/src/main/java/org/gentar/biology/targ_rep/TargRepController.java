package org.gentar.biology.targ_rep;

import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.allele.TargRepAlleleResponseMapper;
import org.gentar.biology.targ_rep.allele.TargRepAlleleService;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellMapper;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellService;
import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.biology.targ_rep.gene.TargRepGeneService;
import org.gentar.biology.targ_rep.pipeline.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.gentar.helpers.LinkUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.IntrospectionUtils.capitalize;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/targ_rep")
@CrossOrigin(origins="*")
public class TargRepController
{
    private final TargRepEsCellMapper esCellMapper;
    private final TargRepPipelineResponseMapper targRepPipelineResponseMapper;
    private final TargRepAlleleResponseMapper targRepAlleleResponseMapper;
    private final TargRepAlleleService alleleService;
    private final TargRepEsCellService esCellService;
    private final TargRepPipelineService targRepPipelineService;
    private final TargRepAlleleService targRepAlleleService;
    private final TargRepGeneService geneService;


    public TargRepController(TargRepEsCellMapper esCellMapper,
                             TargRepPipelineResponseMapper targRepPipelineResponseMapper,
                             TargRepAlleleResponseMapper targRepAlleleResponseMapper,
                             TargRepAlleleService alleleService,
                             TargRepEsCellService esCellService,
                             TargRepPipelineServiceImpl targRepPipelineService,
                             TargRepAlleleService targRepAlleleService,
                             TargRepGeneService geneService)
    {
        this.esCellMapper = esCellMapper;
        this.targRepPipelineResponseMapper = targRepPipelineResponseMapper;
        this.targRepAlleleResponseMapper = targRepAlleleResponseMapper;
        this.alleleService = alleleService;
        this.esCellService = esCellService;
        this.targRepPipelineService = targRepPipelineService;
        this.targRepAlleleService = targRepAlleleService;
        this.geneService = geneService;
    }

    /**
     * Get a specific es cell name.
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_symbol/{marker_symbol}"})
    public List<TargRepEsCellDTO> findEsCellByGene(@PathVariable String marker_symbol)
    {
        TargRepGene gene = geneService.getGeneBySymbolFailIfNull(capitalize(marker_symbol));
        List<TargRepAllele> alleleList = alleleService.getTargRepAllelesByGeneFailIfNull(gene);
        Set<TargRepEsCell> esCellList = alleleList.stream()
                .map(a -> esCellService.getTargRepEscellByAlleleFailsIfNull(a))
                .findAny()
                .stream().flatMap(List::stream).collect(Collectors.toSet());

        return esCellMapper.toDtos(esCellList);
    }

    /**
     * Get a specific es cell name.
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_name/{name}"})
    public TargRepEsCellDTO findEsCellByName(@PathVariable String name)
    {
        TargRepEsCell esCellList = esCellService.getTargRepEsCellByNameFailsIfNull(name);
        return esCellMapper.toDto(esCellList);
    }

    /**
     * Get the targ rep pipelines.
     * @return A collection  of {@link TargRepPipelineResponseDTO} objects.
     */
    @GetMapping(value = {"/pipelines"})
    public ResponseEntity findAllTargRepPipelines(
            final Pageable pageable,
            final PagedResourcesAssembler assembler) {

        Page<TargRepPipeline> targRepPipelines = targRepPipelineService.getPageableTargRepPipeline(pageable);
        Page<TargRepPipelineResponseDTO> targRepPipelineResponseDTOSPage = targRepPipelines.map(this::getDTO);

        PagedModel pr =
                assembler.toModel(targRepPipelineResponseDTOSPage,
                        linkTo(methodOn(TargRepController.class).findAllTargRepPipelines(pageable, assembler)).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private TargRepPipelineResponseDTO getDTO(TargRepPipeline targRepPipeline) {
        TargRepPipelineResponseDTO targRepPipelineResponseDTO = new TargRepPipelineResponseDTO();
        if (targRepPipeline != null) {
            targRepPipelineResponseDTO = targRepPipelineResponseMapper.toDto(targRepPipeline);
        }
        return targRepPipelineResponseDTO;
    }


    /**
     * Get the targ rep alleles.
     * @return A collection  of {@link TargRepAlleleResponseDTO} objects.
     */
    @GetMapping(value = {"/alleles"})
    public ResponseEntity findAllTargRepAlleles(
            final Pageable allelePageable,
            final PagedResourcesAssembler alleleAssembler)
    {

        Page<TargRepAllele> targRepAlleles = targRepAlleleService.getPageableTargRepAllele(allelePageable);
        Page<TargRepAlleleResponseDTO> targRepAlleleResponseDTOSPage = targRepAlleles.map(x -> targRepAlleleResponseMapper.toDto(x));

        PagedModel pr =
                alleleAssembler.toModel(targRepAlleleResponseDTOSPage,
                        linkTo(methodOn(TargRepController.class).findAllTargRepAlleles(allelePageable, alleleAssembler)).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    /**
     * Get a specific targ rep pipeline.
     * @param id Pipeline identifier.
     * @return Entity with the targ rep pipeline information.
     */
    @GetMapping(value = {"/pipelines/{id}"})
    public EntityModel<?> findTargRepPipelineById(@PathVariable Long id)
    {
        EntityModel<TargRepPipelineResponseDTO> entityModel = null;
        TargRepPipeline targRepPipeline = targRepPipelineService.getNotNullTargRepPipelineById(id);
        entityModel = EntityModel.of(targRepPipelineResponseMapper.toDto(targRepPipeline));
        return entityModel;
    }


    /**
     * Get a specific targ rep allele.
     * @param id TargRep Allele identifier.
     * @return Entity with the targ rep allele information.
     */
    @GetMapping(value = {"/alleles/{id}"})
    public EntityModel<?> findTargRepAlleleById(@PathVariable Long id)
    {
        EntityModel<TargRepAlleleResponseDTO> entityModel = null;
        TargRepAllele targRepAllele = targRepAlleleService.getNotNullTargRepAlelleById(id);
        entityModel = EntityModel.of(targRepAlleleResponseMapper.toDto(targRepAllele));
        return entityModel;
    }


}
