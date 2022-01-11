package org.gentar.biology.targ_rep.pipeline;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TargRepPipelineRepository extends PagingAndSortingRepository<TargRepPipeline, Long>
{

    List<TargRepPipeline> findAll();
    TargRepPipeline findTargRepPipelineById(Long id);
}
