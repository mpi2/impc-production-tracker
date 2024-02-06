package org.gentar.biology.targ_rep.es_cell;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.TargRepEsCellResponseDTO;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.allele.TargRepAlleleController;
import org.gentar.biology.targ_rep.es_cell.distribution_qc.DistributionQcMapper;
import org.gentar.biology.targ_rep.mutation.TargRepEsCellMutation;
import org.gentar.biology.targ_rep.mutation.TargRepEsCellMutationService;
import org.gentar.biology.targ_rep.pipeline.TargRepPipeline;
import org.gentar.biology.targ_rep.pipeline.TargRepPipelineController;
import org.gentar.biology.targ_rep.targeting_vector.TargRepTargetingVector;
import org.gentar.biology.targ_rep.targeting_vector.TargRepTargetingVectorController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * TargRepEsCellMapper.
 */
@Component
public class TargRepEsCellMapper implements Mapper<TargRepEsCell, TargRepEsCellResponseDTO> {

    private final DistributionQcMapper distributionQcMapper;
    private final TargRepEsCellMutationService targRepEsCellMutationService;


    public TargRepEsCellMapper(DistributionQcMapper distributionQcMapper,
                               TargRepEsCellMutationService targRepEsCellMutationService) {
        this.distributionQcMapper = distributionQcMapper;
        this.targRepEsCellMutationService = targRepEsCellMutationService;
    }

    @Override
    public TargRepEsCellResponseDTO toDto(TargRepEsCell entity) {
        TargRepEsCellResponseDTO esCellDto = new TargRepEsCellResponseDTO();
        if (entity != null) {
            esCellDto.setId(entity.getId());
            if (entity.getPipeline() != null) {
                esCellDto.setPipelineId(entity.getPipeline().getId());
                addPipelineLink(esCellDto, entity.getPipeline());
            }
            if (entity.getAllele() != null) {
                esCellDto.setAlleleId(entity.getAllele().getId());
                addAlleleLink(esCellDto, entity.getAllele());
            }
            esCellDto.setName(entity.getName());
            if (entity.getTargetingVector() != null) {
                esCellDto.setTargetingVectorId(entity.getTargetingVector().getId());
                addTargetingVectorLink(esCellDto, entity.getTargetingVector());
            }
            esCellDto.setParentalCellLine(entity.getParentalCellLine());
            if (entity.getIkmcProject() != null) {
                esCellDto.setIkmcProjectId(entity.getIkmcProject().getName());

            }
            esCellDto.setComment(entity.getComment());
            esCellDto.setReportToPublic(entity.getReportToPublic());
            esCellDto.setUserQcComment(entity.getUserQcComment());
            esCellDto
                .setUserQcFivePrimeCassetteIntegrity(entity.getUserQcFivePrimeCassetteIntegrity());
            esCellDto.setUserQcFivePrimeLrPcr(entity.getUserQcFivePrimeLrPcr());
            esCellDto.setUserQcKaryotype(entity.getUserQcKaryotype());
            esCellDto.setUserQcLaczSrPcr(entity.getUserQcLaczSrPcr());
            esCellDto.setUserQcLossOfWtAllele(entity.getUserQcLossOfWtAllele());
            esCellDto.setUserQcLoxpConfirmation(entity.getUserQcLoxpConfirmation());
            esCellDto.setUserQcMapTest(entity.getUserQcMapTest());
            esCellDto.setUserQcMutantSpecificSrPcr(entity.getUserQcMutantSpecificSrPcr());
            esCellDto.setUserQcNeoCountQpcr(entity.getUserQcNeoCountQpcr());
            esCellDto.setUserQcNeoSrPcr(entity.getUserQcNeoSrPcr());
            esCellDto.setUserQcSouthernBlot(entity.getUserQcSouthernBlot());
            esCellDto.setUserQcThreePrimeLrPcr(entity.getUserQcThreePrimeLrPcr());
            esCellDto.setUserQcTvBackboneAssay(entity.getUserQcTvBackboneAssay());
            esCellDto = distributionQcMapper.toDistributionQcDto(esCellDto);
            esCellDto = toProductionQcServiceDto(esCellDto);
            addSelfLink(esCellDto, entity);

        }

        return esCellDto;
    }

    @Override
    public TargRepEsCell toEntity(TargRepEsCellResponseDTO dto) {
        return Mapper.super.toEntity(dto);
    }

    private void addSelfLink(TargRepEsCellResponseDTO targRepEsCellResponseDTO,
                             TargRepEsCell targRepEsCell) {
        Link link = linkTo(
            methodOn(TargRepEsCellController.class).findTargRepEsCellById(targRepEsCell.getId()))
            .withSelfRel();
        targRepEsCellResponseDTO.add(link);
    }


    private void addPipelineLink(TargRepEsCellResponseDTO targRepEsCellResponseDTO,
                                 TargRepPipeline targRepPipeline) {
        Link link = linkTo(methodOn(TargRepPipelineController.class)
            .findTargRepPipelineById(targRepPipeline.getId())).withRel("pipeline");
        link = link.withHref(decode(link.getHref()));
        targRepEsCellResponseDTO.add(link);
    }

    private void addAlleleLink(TargRepEsCellResponseDTO targRepEsCellResponseDTO,
                               TargRepAllele targRepAllele) {
        Link link = linkTo(
            methodOn(TargRepAlleleController.class).findTargRepAlleleById(targRepAllele.getId()))
            .withRel("allele");
        link = link.withHref(decode(link.getHref()));
        targRepEsCellResponseDTO.add(link);
    }

    private void addTargetingVectorLink(TargRepEsCellResponseDTO targRepEsCellResponseDTO,
                                        TargRepTargetingVector targRepTargetingVector) {
        Link link = linkTo(
            methodOn(TargRepTargetingVectorController.class)
                .findTargRepTargetingVectorById(targRepTargetingVector.getId()))
            .withRel("targeting_vector");
        link = link.withHref(decode(link.getHref()));
        targRepEsCellResponseDTO.add(link);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }


    public TargRepEsCellResponseDTO toProductionQcServiceDto(TargRepEsCellResponseDTO esCellDto) {
        TargRepEsCellMutation targRepEsCellMutation =
            getTargRepEsCellMutationByEcCellId(esCellDto.getId());
        if (targRepEsCellMutation != null) {
            esCellDto.setProductionQcFivePrimeScreen(targRepEsCellMutation.getFivePrimeScreen());
            esCellDto.setProductionQcLossOfAllele(targRepEsCellMutation.getLossOfAllele());
            esCellDto.setProductionQcLoxpScreen(targRepEsCellMutation.getLoxpScreen());
            esCellDto.setProductionQcThreePrimeScreen(targRepEsCellMutation.getThreePrimeScreen());
            esCellDto.setProductionQcVectorIntegrity(targRepEsCellMutation.getVectorIntegrity());
        }
        return esCellDto;
    }


    private TargRepEsCellMutation getTargRepEsCellMutationByEcCellId(Long esCEllId) {
        return targRepEsCellMutationService.getTargRepEsCellMutationByEsCellId(esCEllId);
    }
}
