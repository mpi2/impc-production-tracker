package org.gentar.biology.targ_rep.pipeline;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanResponseDTO;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectController;
import org.gentar.biology.targ_rep.TargRepController;
import org.gentar.biology.targ_rep.TargRepPipelineResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TargRepPipelineResponseMapper implements Mapper<TargRepPipeline, TargRepPipelineResponseDTO> {

    @Override
    public TargRepPipelineResponseDTO toDto(TargRepPipeline entity) {
        TargRepPipelineResponseDTO targRepPipelineDTO = new TargRepPipelineResponseDTO();
        if (entity != null) {
            targRepPipelineDTO.setId(entity.getId());
            targRepPipelineDTO.setName(entity.getName());
            targRepPipelineDTO.setDescription(entity.getDescription());
            targRepPipelineDTO.setGeneTrap(entity.getGeneTrap());
            targRepPipelineDTO.setReportToPublic(entity.getReportToPublic());
            addSelfLink(targRepPipelineDTO, entity);
        }
        return targRepPipelineDTO;
    }

    @Override
    public TargRepPipeline toEntity(TargRepPipelineResponseDTO dto) {
        return Mapper.super.toEntity(dto);
    }

    private void addSelfLink(TargRepPipelineResponseDTO targRepPipelineDTO, TargRepPipeline targRepPipeline) {
        Link link = linkTo(methodOn(TargRepController.class).findTargRepPipelineById(targRepPipeline.getId())).withSelfRel();
        targRepPipelineDTO.add(link);
    }
}
