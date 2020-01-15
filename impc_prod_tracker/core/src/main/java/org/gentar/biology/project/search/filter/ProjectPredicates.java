package org.gentar.biology.project.search.filter;

import org.gentar.biology.allele.AlleleType;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.intention.project_intention.ProjectIntention;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectPredicates
{
    public boolean inTpn(Project project, List<String> values)
    {
        return values.contains(project.getTpn());
    }

    public boolean inWorkUnitName(Project project, List<String> values)
    {
        WorkUnit match = null;
        if (project != null)
        {
            List<WorkUnit> workUnitsByProject = project.getRelatedWorkUnits();
            match = workUnitsByProject.stream()
                .filter(wu -> values.contains(wu.getName()))
                .findAny().orElse(null);
        }
        return match != null;
    }

    public boolean inWorkGroupName(Project project, List<String> values)
    {
        WorkGroup match = null;
        if (project != null)
        {
            List<WorkGroup> workGroupsByProject = project.getRelatedWorkGroups();
            match = workGroupsByProject.stream()
                .filter(wg -> values.contains(wg.getName()))
                .findAny().orElse(null);
        }
        return match != null;
    }

    public boolean inExternalReference(Project project, List<String> values)
    {
        return values.contains(project.getProjectExternalRef());
    }

    public boolean inIntentions(Project project, List<String> values)
    {
        List<AlleleType> alleleTypes = getProjectAlleleTypes(project);
        return alleleTypes.stream()
            .filter(x -> values.contains(x.getName()))
            .findFirst().orElse(null) != null;
    }

    private List<AlleleType> getProjectAlleleTypes(Project project)
    {
        List<AlleleType> alleleTypes = new ArrayList<>();
        if (project != null)
        {
            List<ProjectIntention> projectIntentions = project.getProjectIntentions();
            if (projectIntentions != null)
            {
                projectIntentions.forEach(i -> alleleTypes.add(i.getAlleleType()));
            }
        }
        return alleleTypes;
    }

    public boolean inConsortiaNames(Project project, List<String> values)
    {
        boolean anyMatch = false;
        if (project != null)
        {
            var relatedConsortia = project.getRelatedConsortia();
            anyMatch = relatedConsortia.stream()
                .filter(c -> values.contains(c.getName()))
                .findFirst().orElse(null) != null;
        }
        return anyMatch;
    }

    public boolean inPrivaciesNames(Project project, List<String> values)
    {
        return values.contains(project.getPrivacy().getName());
    }
}
