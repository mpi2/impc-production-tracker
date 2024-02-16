package org.gentar.biology.targ_rep.es_cell;

import org.gentar.biology.targ_rep.TargRepEsCellLegacyResponseDTO;
import org.gentar.biology.targ_rep.TargRepEsCellResponseDTO;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.allele.TargRepAlleleService;
import org.gentar.biology.targ_rep.es_cell.distribution_qc.DistributionQcMapper;
import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.biology.targ_rep.gene.TargRepGeneService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.IntrospectionUtils.capitalize;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * TargRepEsCellController Class.
 */
@RestController
@RequestMapping("/api/targ_rep")
@CrossOrigin(origins = "*")
public class TargRepEsCellController {
    private final TargRepEsCellMapper esCellMapper;
    private final DistributionQcMapper distributionQcMapper;
    private final TargRepEsCellLegacyMapper targRepEsCellLegacyMapper;
    private final TargRepEsCellService esCellService;
    private final TargRepGeneService geneService;
    private final TargRepAlleleService alleleService;

    /**
     * TargRepEsCellController Cons.
     */
    public TargRepEsCellController(
        TargRepEsCellMapper esCellMapper,
        DistributionQcMapper distributionQcMapper,
        TargRepEsCellLegacyMapper targRepEsCellLegacyMapper,
        TargRepEsCellService esCellService,
        TargRepGeneService geneService,
        TargRepAlleleService alleleService) {
        this.esCellMapper = esCellMapper;
        this.distributionQcMapper = distributionQcMapper;
        this.targRepEsCellLegacyMapper = targRepEsCellLegacyMapper;
        this.esCellService = esCellService;
        this.geneService = geneService;
        this.alleleService = alleleService;
    }

    /**
     * Get a specific es cell name.
     *
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_symbol/{marker_symbol}"})
    public List<TargRepEsCellLegacyResponseDTO> findEsCellByGene(
        @PathVariable String marker_symbol) {
        TargRepGene gene = geneService.getGeneBySymbolFailIfNull(capitalize(marker_symbol));
        List<TargRepAllele> alleleList = alleleService.getTargRepAllelesByGeneFailIfNull(gene);
        List<TargRepEsCell> esCellList = new ArrayList();
        alleleList
            .forEach(allele -> esCellList
                .addAll(esCellService.getTargRepEscellByAlleleFailsIfNull(allele)));

        return esCellList.stream().map(targRepEsCellLegacyMapper::toDto)
            .collect(Collectors.toList());

    }

    /**
     * Get a specific es cell name.
     *
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_name/{name}"})
    public TargRepEsCellLegacyResponseDTO findEsCellByName(@PathVariable String name) {
        TargRepEsCell esCellList = esCellService.getTargRepEsCellByNameFailsIfNull(name);
        return targRepEsCellLegacyMapper.toDto(esCellList);
    }

    /**
     * Get the targ rep es cell.
     *
     * @return A collection  of {@link TargRepEsCellResponseDTO} objects.
     */
    @GetMapping(value = {"/es_cell"})
    public ResponseEntity findAllTargRepEsCells(
        final Pageable esCellPageable,
        final PagedResourcesAssembler esCellAssembler) {

        Page<TargRepEsCell> targRepEsCells = esCellService.getPageableTargRepEsCell(esCellPageable);
        Page<TargRepEsCellResponseDTO> targRepEsCellDTOPage =
            targRepEsCells.map(esCellMapper::toDto);

        Link link = linkTo(methodOn(TargRepEsCellController.class)
            .findAllTargRepEsCells(esCellPageable, esCellAssembler)).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        PagedModel pr =
            esCellAssembler.toModel(targRepEsCellDTOPage,
                link);

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
    @GetMapping(value = {"/es_cell/{id}"})
    public EntityModel<TargRepEsCellResponseDTO> findTargRepEsCellById(@PathVariable Long id) {

        EntityModel<TargRepEsCellResponseDTO> entityModel;
        TargRepEsCell targRepEsCell = esCellService.getTargRepEsCellById(id);
        entityModel = EntityModel.of(esCellMapper.toDto(targRepEsCell));
        return entityModel;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
