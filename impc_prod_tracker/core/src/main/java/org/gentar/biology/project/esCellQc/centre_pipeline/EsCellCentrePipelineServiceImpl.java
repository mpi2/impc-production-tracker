package org.gentar.biology.project.esCellQc.centre_pipeline;

import org.springframework.stereotype.Component;

@Component
public class EsCellCentrePipelineServiceImpl implements EsCellCentrePipelineService
{
    private final EsCellCentrePipelineRepository esCellCentrePipelineRepository;

    public EsCellCentrePipelineServiceImpl(EsCellCentrePipelineRepository esCellCentrePipelineRepository) {
        this.esCellCentrePipelineRepository = esCellCentrePipelineRepository;
    }

    @Override
    public EsCellCentrePipeline getEsCellCentrePipelineByName(String name) {
        return esCellCentrePipelineRepository.findFirstByNameIgnoreCase(name);
    }
}
