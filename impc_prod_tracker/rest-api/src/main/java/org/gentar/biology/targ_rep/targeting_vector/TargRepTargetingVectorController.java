package org.gentar.biology.targ_rep.targeting_vector;

import org.gentar.biology.colony.distribution.distribution_network.DistributionNetworkService;
import org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product.TargRepTargetingVectorDistributionProduct;
import org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product.TargRepTargetingVectorDistributionProductService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.helpers.LinkUtil;
import org.gentar.security.abac.subject.SubjectRetriever;
import org.gentar.security.abac.subject.SystemSubject;
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

    private final DistributionNetworkService distributionNetworkService;

    private final TargRepTargetingVectorDistributionProductService distributionProductService;



    private final SubjectRetriever subjectRetriever;


    public TargRepTargetingVectorController(
            TargRepTargetingVectorResponseMapper targetingVectorResponseMapper,
            TargRepTargetingVectorService targetingVectorService, DistributionNetworkService distributionNetworkService, TargRepTargetingVectorDistributionProductService distributionProductService, SubjectRetriever subjectRetriever) {
        this.targetingVectorResponseMapper = targetingVectorResponseMapper;
        this.targetingVectorService = targetingVectorService;
        this.distributionNetworkService = distributionNetworkService;
        this.distributionProductService = distributionProductService;
        this.subjectRetriever = subjectRetriever;
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


    @PutMapping(value = {"/targeting_vectors/distribution_product"})
    public Boolean updateTargRepTargetingVectorDistributionProduct(
            @RequestBody TargRepTargetingVectorDistributionProductUpdateRequestDTO dto) {

        SystemSubject systemSubject = subjectRetriever.getSubject();

        boolean canUpdate = systemSubject.isAdmin() || systemSubject.getPerson().getEmail().equals("jraller+apins@ucdavis.edu");
        if (!canUpdate) {
            throw new UserOperationFailedException("You don have permission for this operation");
        }

        if (!distributionProductService.getByDistributionIdentifier(dto.getDistributionIdentifier()).isEmpty()) {
            throw new UserOperationFailedException(
                    String.format("distribution Identifier [%s] is already registered", dto.getDistributionIdentifier()));
        }
        TargRepTargetingVector targetingVector = targetingVectorService.getTargRepTargetingVectorByNameFailsIfNull(dto.getTargetingVectorName());
        TargRepTargetingVectorDistributionProduct targRepTargetingVectorDistributionProduct = new TargRepTargetingVectorDistributionProduct();
        if (distributionNetworkService.getDistributionNetworkByName(dto.getDistributionNetworkName()) == null) {

            throw new UserOperationFailedException(
                    String.format("distribution Network name [%s] does not exist.", dto.getDistributionNetworkName()));
        }
        targRepTargetingVectorDistributionProduct.setDistributionNetwork(distributionNetworkService.getDistributionNetworkByName(dto.getDistributionNetworkName()));
        targRepTargetingVectorDistributionProduct.setTargRepTargetingVector(targetingVector);
        targRepTargetingVectorDistributionProduct.setDistributionIdentifier(dto.getDistributionIdentifier());
        targRepTargetingVectorDistributionProduct.setStartDate(dto.getStartDate());
        targRepTargetingVectorDistributionProduct.setEndDate(dto.getEndDate());
        targetingVector.getTargRepTargetingVectorDistributionProducts().add(targRepTargetingVectorDistributionProduct);
        return targetingVectorService.save(targetingVector) != null;
    }


    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
