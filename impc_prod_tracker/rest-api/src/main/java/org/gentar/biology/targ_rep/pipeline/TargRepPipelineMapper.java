package org.gentar.biology.targ_rep.pipeline;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.TargRepPipelineResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TargRepPipelineMapper  implements Mapper<TargRepPipeline, TargRepPipelineResponseDTO> {

    @Override
    public TargRepPipelineResponseDTO toDto(TargRepPipeline entity) {
        TargRepPipelineResponseDTO targRepPipelineDTO = new TargRepPipelineResponseDTO();
        if (entity != null)
        {
            targRepPipelineDTO.setId(entity.getId());
            targRepPipelineDTO.setName(entity.getName());
            targRepPipelineDTO.setDescription(entity.getDescription());
            targRepPipelineDTO.setGeneTrap(entity.getGeneTrap());
            targRepPipelineDTO.setReportToPublic(entity.getReportToPublic());
        }
        return targRepPipelineDTO;
    }

    @Override
    public TargRepPipeline toEntity(TargRepPipelineResponseDTO dto) {
        return Mapper.super.toEntity(dto);
    }
}
