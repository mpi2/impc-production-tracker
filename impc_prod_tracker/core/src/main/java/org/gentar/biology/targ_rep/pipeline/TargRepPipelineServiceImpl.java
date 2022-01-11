package org.gentar.biology.targ_rep.pipeline;

import org.gentar.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TargRepPipelineServiceImpl implements TargRepPipelineService {

    private final TargRepPipelineRepository targRepPipelineRepository;

    private static final String TARG_REP_PIPELINE_NOT_EXISTS_ERROR =
            "The targ rep pipeline [%s] does not exist.";

    public TargRepPipelineServiceImpl(TargRepPipelineRepository targRepPipelineRepository) {
        this.targRepPipelineRepository = targRepPipelineRepository;
    }

    @Override
    public TargRepPipeline getNotNullTargRepPipelineById(Long id) throws NotFoundException {
        TargRepPipeline targRepPipeline = targRepPipelineRepository.findTargRepPipelineById(id);
        if (targRepPipeline == null)
        {
            throw new NotFoundException(String.format(TARG_REP_PIPELINE_NOT_EXISTS_ERROR, id));
        }
        return targRepPipeline;
    }
}
