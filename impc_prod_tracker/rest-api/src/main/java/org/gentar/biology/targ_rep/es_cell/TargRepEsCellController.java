package org.gentar.biology.targ_rep.es_cell;

import org.gentar.biology.colony.distribution.distribution_network.DistributionNetworkService;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.allele.TargRepAlleleService;
import org.gentar.biology.targ_rep.distribution.es_cell_distribution_product.TargRepEsCellDistributionProduct;
import org.gentar.biology.targ_rep.distribution.es_cell_distribution_product.TargRepEsCellDistributionProductService;
import org.gentar.biology.targ_rep.gene.TargRepGene;
import org.gentar.biology.targ_rep.gene.TargRepGeneService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.helpers.LinkUtil;
import org.gentar.security.abac.subject.SubjectRetriever;
import org.gentar.security.abac.subject.SystemSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
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
    private final TargRepEsCellService esCellService;
    private final TargRepGeneService geneService;
    private final TargRepAlleleService alleleService;

    private final DistributionNetworkService distributionNetworkService;

    private final TargRepEsCellDistributionProductService distributionProductService;

    private final SubjectRetriever subjectRetriever;

    /**
     * TargRepEsCellController Cons.
     */
    public TargRepEsCellController(
            TargRepEsCellMapper esCellMapper,
            TargRepEsCellService esCellService,
            TargRepGeneService geneService,
            TargRepAlleleService alleleService,
            DistributionNetworkService distributionNetworkService,
            TargRepEsCellDistributionProductService distributionProductService,
            SubjectRetriever subjectRetriever) {
        this.esCellMapper = esCellMapper;
        this.esCellService = esCellService;
        this.geneService = geneService;
        this.alleleService = alleleService;
        this.distributionNetworkService = distributionNetworkService;
        this.distributionProductService = distributionProductService;
        this.subjectRetriever = subjectRetriever;
    }

    /**
     * Get a specific es cell name.
     *
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_symbol/{marker_symbol}"})
    public ResponseEntity<CollectionModel<TargRepEsCellResponseDTO>> findEsCellByGene(
            @PathVariable String marker_symbol) {
        TargRepGene gene = geneService.getGeneBySymbolFailIfNull(capitalize(marker_symbol));
        List<TargRepAllele> alleleList = alleleService.getTargRepAllelesByGeneFailIfNull(gene);
        List<TargRepEsCell> esCellList = new ArrayList<>();
        alleleList
                .forEach(allele -> esCellList
                        .addAll(esCellService.getTargRepEscellByAlleleFailsIfNull(allele)));

        List<TargRepEsCellResponseDTO> dtos = esCellList.stream()
                .map(esCellMapper::toDto)
                .collect(Collectors.toList());
        
        Link link = linkTo(methodOn(TargRepEsCellController.class)
                .findEsCellByGene(marker_symbol)).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        
        CollectionModel<TargRepEsCellResponseDTO> collectionModel = 
                CollectionModel.of(dtos, link);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    /**
     * Get a specific es cell name.
     *
     * @return Entity with the es cell names for a gene.
     */
    @GetMapping(value = {"/es_cell_by_name/{name}"})
    public TargRepEsCellResponseDTO findEsCellByName(@PathVariable String name) {
        TargRepEsCell esCellList = esCellService.getTargRepEsCellByNameFailsIfNull(name);
        return esCellMapper.toDto(esCellList);
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


    @PutMapping(value = {"/es_cell/distribution_product"})
    public Boolean updateTargRepEsCellDistributionProduct(
            @RequestBody TargRepEsCellDistributionProductUpdateRequestDTO dto) {
        SystemSubject systemSubject = subjectRetriever.getSubject();

        boolean canUpdate =systemSubject.isAdmin() || systemSubject.getPerson().getEmail().equals("jraller+apins@ucdavis.edu");

        if (!canUpdate) {
            throw new UserOperationFailedException("You don have permission for this operation");
        }

        if (!distributionProductService.getByDistributionIdentifier(dto.getDistributionIdentifier()).isEmpty()) {
            throw new UserOperationFailedException(
                    String.format("distribution Identifier [%s] is already registered", dto.getDistributionIdentifier()));
        }
        TargRepEsCell esCell = esCellService.getTargRepEsCellByNameFailsIfNull(dto.getEsCellName());
        TargRepEsCellDistributionProduct targRepEsCellDistributionProduct = new TargRepEsCellDistributionProduct();
        if (distributionNetworkService.getDistributionNetworkByName(dto.getDistributionNetworkName()) == null) {

            throw new UserOperationFailedException(
                    String.format("distribution Network name [%s] does not exist.", dto.getDistributionNetworkName()));
        }
        targRepEsCellDistributionProduct.setDistributionNetwork(distributionNetworkService.getDistributionNetworkByName(dto.getDistributionNetworkName()));
        targRepEsCellDistributionProduct.setTargRepEsCell(esCell);
        targRepEsCellDistributionProduct.setDistributionIdentifier(dto.getDistributionIdentifier());
        targRepEsCellDistributionProduct.setStartDate(dto.getStartDate());
        targRepEsCellDistributionProduct.setEndDate(dto.getEndDate());
        esCell.getTargRepEsCellDistributionProducts().add(targRepEsCellDistributionProduct);
        return esCellService.save(esCell) != null;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }


}
