package org.gentar.biology.targ_rep.pipeline;

import org.gentar.Mapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
        Link link = linkTo(methodOn(TargRepPipelineController.class).findTargRepPipelineById(targRepPipeline.getId())).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        targRepPipelineDTO.add(link);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
