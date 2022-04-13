package org.gentar.biology.targ_rep.pipeline;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TargRepPipelineService.
 */
public interface TargRepPipelineService {

    TargRepPipeline getNotNullTargRepPipelineById(Long id);

    Page<TargRepPipeline> getPageableTargRepPipeline(Pageable page);
}
