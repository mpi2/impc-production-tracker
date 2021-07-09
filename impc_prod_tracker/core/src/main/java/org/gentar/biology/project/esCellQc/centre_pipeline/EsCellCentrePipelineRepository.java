package org.gentar.biology.project.esCellQc.centre_pipeline;

import org.springframework.data.repository.CrudRepository;

public interface EsCellCentrePipelineRepository extends CrudRepository<EsCellCentrePipeline, Long>
{
    EsCellCentrePipeline findFirstByNameIgnoreCase(String name);
}
