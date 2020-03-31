package org.gentar.biology.plan;

import org.gentar.biology.project.ProjectService;
import org.gentar.util.BeanUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.PostUpdate;

/**
 * A dedicated class to execute logic pre or post a plan is updated.
 */
@Component
public class PlanListener
{
    @PostUpdate
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void notifyProjectOfChanges(Plan plan)
    {
        ProjectService projectService = BeanUtil.getBean(ProjectService.class);
        projectService.checkForUpdates(plan.getProject());
    }
}
