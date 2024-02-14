package org.gentar.biology.targ_rep.targeting_vector;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.allele.TargRepAlleleController;
import org.gentar.biology.targ_rep.allele.TargRepAlleleResponseDTO;
import org.gentar.biology.targ_rep.allele.mutation_subtype.TargRepMutationSubtype;
import org.gentar.biology.targ_rep.allele.mutation_type.TargRepMutationType;
import org.gentar.biology.targ_rep.ikmc_project.TargRepIkmcProject;
import org.gentar.biology.targ_rep.ikmc_project.TargRepIkmcProjectDTO;
import org.gentar.biology.targ_rep.ikmc_project.TargRepIkmcProjectStatusDTO;
import org.gentar.biology.targ_rep.ikmc_project.status.TargRepIkmcProjectStatus;
import org.gentar.biology.targ_rep.mutation.TargRepMutationSubtypeDTO;
import org.gentar.biology.targ_rep.mutation.TargRepMutationTypeDTO;
import org.gentar.biology.targ_rep.pipeline.TargRepPipeline;
import org.gentar.biology.targ_rep.pipeline.TargRepPipelineController;
import org.gentar.biology.targ_rep.pipeline.TargRepPipelineResponseDTO;
import org.gentar.biology.targ_rep.targeting_vector.es_cell_distribution_product.TargRepTargetingVectorDistributionProductMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * TargRepAlleleResponseMapper.
 */
@Component
public class TargRepTargetingVectorResponseMapper
        implements Mapper<TargRepTargetingVector, TargRepTargetingVectorResponseDTO> {


    @Override
    public TargRepTargetingVectorResponseDTO toDto(TargRepTargetingVector entity) {
        TargRepTargetingVectorResponseDTO targetingVectorDTO =
                new TargRepTargetingVectorResponseDTO();
        if (entity != null) {
            targetingVectorDTO.setId(entity.getId());
            targetingVectorDTO.setName(entity.getName());
            targetingVectorDTO.setIntermediateVector(entity.getIntermediateVector());
            targetingVectorDTO.setReportToPublic(entity.getReportToPublic());
            targetingVectorDTO.setAlleleTypePrediction(entity.getAlleleTypePrediction());
            targetingVectorDTO.setIkmcProjectName(entity.getIkmcProjectName());
            targetingVectorDTO.setMgiAlleleNamePrediction(entity.getMgiAlleleNamePrediction());
            targetingVectorDTO.setPipeline(getTargRepPipelineResponseDTO(entity.getPipeline()));
            targetingVectorDTO.setIkmcProject(getTargRepIkmcProjectDTO(entity.getIkmcProject()));
            targetingVectorDTO.setAllele(getTargRepAlleleResponseDTO(entity.getAllele()));
            targetingVectorDTO.setTargRepTargetingVectorDistributionProductList(TargRepTargetingVectorDistributionProductMapper.mapToDto(entity.getTargRepTargetingVectorDistributionProducts()));
            addSelfLink(targetingVectorDTO, entity);
        }

        return targetingVectorDTO;
    }


    @Override
    public TargRepTargetingVector toEntity(TargRepTargetingVectorResponseDTO dto) {
        return Mapper.super.toEntity(dto);
    }

    private TargRepPipelineResponseDTO getTargRepPipelineResponseDTO(
            TargRepPipeline targRepPipeline) {
        TargRepPipelineResponseDTO targRepPipelineResponseDTO =
                new TargRepPipelineResponseDTO();
        if (targRepPipeline != null) {

            targRepPipelineResponseDTO.setId(targRepPipeline.getId());
            targRepPipelineResponseDTO.setName(targRepPipeline.getName());
            targRepPipelineResponseDTO.setReportToPublic(targRepPipeline.getReportToPublic());
            targRepPipelineResponseDTO.setDescription(targRepPipeline.getDescription());
            targRepPipelineResponseDTO.setGeneTrap(targRepPipeline.getGeneTrap());
            addPipelineLink(targRepPipelineResponseDTO, targRepPipeline);
        }
        return targRepPipelineResponseDTO;
    }

    private TargRepAlleleResponseDTO getTargRepAlleleResponseDTO(
            TargRepAllele targRepAllele) {
        TargRepAlleleResponseDTO targRepAlleleResponseDTO = new TargRepAlleleResponseDTO();
        if (targRepAllele != null) {

            targRepAlleleResponseDTO.setId(targRepAllele.getId());
            targRepAlleleResponseDTO.setAssembly(targRepAllele.getAssembly());
            targRepAlleleResponseDTO.setBackbone(targRepAllele.getBackbone());
            targRepAlleleResponseDTO.setCassette(targRepAllele.getCassette());
            targRepAlleleResponseDTO.setCassetteEnd(targRepAllele.getCassetteEnd());
            targRepAlleleResponseDTO.setCassetteStart(targRepAllele.getCassetteStart());
            targRepAlleleResponseDTO.setCassetteType(targRepAllele.getCassetteType());
            targRepAlleleResponseDTO.setChromosome(targRepAllele.getChromosome());
            targRepAlleleResponseDTO.setFloxedEndExon(targRepAllele.getFloxedEndExon());
            targRepAlleleResponseDTO.setFloxedStartExon(targRepAllele.getFloxedStartExon());
            targRepAlleleResponseDTO.setHomologyArmEnd(targRepAllele.getHomologyArmEnd());
            targRepAlleleResponseDTO.setHomologyArmStart(targRepAllele.getHomologyArmStart());
            targRepAlleleResponseDTO.setLoxpEnd(targRepAllele.getLoxpEnd());
            targRepAlleleResponseDTO.setLoxpStart(targRepAllele.getLoxpStart());
            if (targRepAllele.getMutationSubtype() != null) {
                targRepAlleleResponseDTO
                        .setMutationSubtype(
                                getTargRepMutationSubtypeDTO(targRepAllele.getMutationSubtype()));
            }
            targRepAlleleResponseDTO
                    .setMutationType(getTargRepMutationTypeDTO(targRepAllele.getMutationType()));
            targRepAlleleResponseDTO.setProjectDesignId(targRepAllele.getProjectDesignId());
            targRepAlleleResponseDTO.setStrand(targRepAllele.getStrand());
            targRepAlleleResponseDTO.setSubtypeDescription(targRepAllele.getSubtypeDescription());

            addAlleleLink(targRepAlleleResponseDTO, targRepAllele);
        }
        return targRepAlleleResponseDTO;
    }

    private TargRepIkmcProjectDTO getTargRepIkmcProjectDTO(
            TargRepIkmcProject targRepIkmcProject) {
        TargRepIkmcProjectDTO targRepIkmcProjectDTO = new TargRepIkmcProjectDTO();
        if (targRepIkmcProject != null) {
            targRepIkmcProjectDTO.setId(targRepIkmcProject.getId());
            targRepIkmcProjectDTO.setName(targRepIkmcProject.getName());
            targRepIkmcProjectDTO.setPipelineId(targRepIkmcProject.getPipeline().getId());
            targRepIkmcProjectDTO
                    .setStatus(getTargRepIkmcProjectStatusDTO(targRepIkmcProject.getStatus()));
        }
        return targRepIkmcProjectDTO;
    }


    private TargRepIkmcProjectStatusDTO getTargRepIkmcProjectStatusDTO(
            TargRepIkmcProjectStatus targRepIkmcProjectStatus) {
        TargRepIkmcProjectStatusDTO targRepIkmcProjectStatusDTO = new TargRepIkmcProjectStatusDTO();
        if (targRepIkmcProjectStatus != null) {
            targRepIkmcProjectStatusDTO.setName(targRepIkmcProjectStatus.getName());
        }
        return targRepIkmcProjectStatusDTO;
    }


    private TargRepMutationTypeDTO getTargRepMutationTypeDTO(
            TargRepMutationType targRepMutationType) {
        TargRepMutationTypeDTO targRepMutationTypeDTO = new TargRepMutationTypeDTO();
        if (targRepMutationType != null) {
            targRepMutationTypeDTO.setName(targRepMutationType.getName());
        }
        return targRepMutationTypeDTO;
    }


    private TargRepMutationSubtypeDTO getTargRepMutationSubtypeDTO(
            TargRepMutationSubtype targRepMutationSubtype) {
        TargRepMutationSubtypeDTO targRepMutationSubtypeDTO = new TargRepMutationSubtypeDTO();
        if (targRepMutationSubtype != null) {
            targRepMutationSubtypeDTO.setName(targRepMutationSubtype.getName());
        }
        return targRepMutationSubtypeDTO;
    }


    private void addSelfLink(TargRepTargetingVectorResponseDTO targetingVectorResponseDTO,
                             TargRepTargetingVector targRepTargetingVector) {
        Link link = linkTo(methodOn(TargRepTargetingVectorController.class)
                .findTargRepTargetingVectorById(targRepTargetingVector.getId())).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        targetingVectorResponseDTO.add(link);
    }

    private void addPipelineLink(TargRepPipelineResponseDTO targRepPipelineDTO,
                                 TargRepPipeline targRepPipeline) {
        Link link = linkTo(methodOn(TargRepPipelineController.class)
                .findTargRepPipelineById(targRepPipeline.getId())).withRel("pipeline");
        link = link.withHref(decode(link.getHref()));
        targRepPipelineDTO.add(link);
    }


    private void addAlleleLink(TargRepAlleleResponseDTO targRepAlleleResponseDto,
                               TargRepAllele targRepAllele) {
        Link link = linkTo(
                methodOn(TargRepAlleleController.class).findTargRepAlleleById(targRepAllele.getId()))
                .withRel("allele");
        link = link.withHref(decode(link.getHref()));
        targRepAlleleResponseDto.add(link);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
