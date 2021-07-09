package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.project.esCellQc.centre_pipeline.EsCellCentrePipeline;
import org.gentar.biology.project.esCellQc.centre_pipeline.EsCellCentrePipelineService;
import org.springframework.stereotype.Component;

@Component
public class EsCellReceivedFromMapper implements Mapper<EsCellCentrePipeline, String>
{
    private final EsCellCentrePipelineService esCellCentrePipelineService;

    public EsCellReceivedFromMapper(EsCellCentrePipelineService esCellCentrePipelineService)
    {
        this.esCellCentrePipelineService = esCellCentrePipelineService;
    }

    @Override
    public String toDto(EsCellCentrePipeline entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public EsCellCentrePipeline toEntity(String name)
    {
        return esCellCentrePipelineService.getEsCellCentrePipelineByName(name);
    }
}
