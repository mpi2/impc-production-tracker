package org.gentar.biology.targ_rep.ikmc_project;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.TargRepIkmcProjectDTO;
import org.gentar.biology.targ_rep.TargRepIkmcProjectStatusDTO;
import org.gentar.biology.targ_rep.pipeline.TargRepPipeline;
import org.gentar.biology.targ_rep.pipeline.TargRepPipelineService;
import org.springframework.stereotype.Component;

@Component
public class TargRepIkmcProjectMapper implements Mapper<TargRepIkmcProject, TargRepIkmcProjectDTO> {

    private final TargRepIkmcProjectStatusMapper targRepIkmcProjectStatusMapper;
    private final TargRepPipelineService targRepPipelineService;

    public TargRepIkmcProjectMapper(TargRepIkmcProjectStatusMapper targRepIkmcProjectStatusMapper,
                                    TargRepPipelineService targRepPipelineService) {
        this.targRepIkmcProjectStatusMapper = targRepIkmcProjectStatusMapper;
        this.targRepPipelineService = targRepPipelineService;
    }
    @Override
    public TargRepIkmcProjectDTO toDto(TargRepIkmcProject entity){
        TargRepIkmcProjectDTO targRepIkmcProjectDTO = new TargRepIkmcProjectDTO();

        targRepIkmcProjectDTO.setId(entity.getId());
        targRepIkmcProjectDTO.setName(entity.getName());
        targRepIkmcProjectDTO.setPipelineId(entity.getPipeline().getId());

        setTargRepIkmcProjectStatusDTO(targRepIkmcProjectDTO, entity);

        return targRepIkmcProjectDTO;
    }

    @Override
    public TargRepIkmcProject toEntity(TargRepIkmcProjectDTO targRepIkmcProjectDTO)
    {
        TargRepIkmcProject targRepIkmcProject = new TargRepIkmcProject();

        targRepIkmcProject.setId(targRepIkmcProjectDTO.getId());
        targRepIkmcProject.setName(targRepIkmcProjectDTO.getName());

        setTargRepIkmcProjectPipelineIdToEntity(targRepIkmcProject, targRepIkmcProjectDTO);
        setTargRepIkmcProjectStatusToEntity(targRepIkmcProject, targRepIkmcProjectDTO);

        return targRepIkmcProject;
    }

    private void setTargRepIkmcProjectPipelineIdToEntity(TargRepIkmcProject targRepIkmcProject,
                                                         TargRepIkmcProjectDTO targRepIkmcProjectDTO)
    {
        TargRepPipeline targRepPipeline =
                targRepPipelineService.getNotNullTargRepPipelineById(targRepIkmcProjectDTO.getPipelineId());
        targRepIkmcProject.setPipeline(targRepPipeline);

    }

    private void setTargRepIkmcProjectStatusToEntity(TargRepIkmcProject targRepIkmcProject,
                                                     TargRepIkmcProjectDTO targRepIkmcProjectDTO)
    {
        targRepIkmcProject.setStatus(targRepIkmcProjectStatusMapper.toEntity(targRepIkmcProjectDTO.getStatus()));

    }

    private void setTargRepIkmcProjectStatusDTO(TargRepIkmcProjectDTO targRepIkmcProjectDTO,
                                                TargRepIkmcProject targRepIkmcProject)
    {
        TargRepIkmcProjectStatusDTO targRepIkmcProjectStatusDTO =
                targRepIkmcProjectStatusMapper.toDto(targRepIkmcProject.getStatus());
        targRepIkmcProjectDTO.setStatus(targRepIkmcProjectStatusDTO);
    }

}
