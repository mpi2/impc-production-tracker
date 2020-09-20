package org.gentar.biology.intention.project_intention;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class ProjectIntentionServiceImpl implements ProjectIntentionService {

    private static final String PROJECT_INTENTION_NULL_ERROR = "Select at least one project intention.";

    @Override
    public void getErrorProjectIntentionNull()
    {
        throw new UserOperationFailedException(
                String.format(PROJECT_INTENTION_NULL_ERROR));
    }
}
