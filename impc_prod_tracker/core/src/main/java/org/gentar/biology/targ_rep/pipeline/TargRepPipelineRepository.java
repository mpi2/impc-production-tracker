package org.gentar.biology.targ_rep.pipeline;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * TargRepPipelineRepository.
 */
public interface TargRepPipelineRepository
    extends PagingAndSortingRepository<TargRepPipeline, Long> {

    List<TargRepPipeline> findAll();

    TargRepPipeline findTargRepPipelineById(Long id);
}
