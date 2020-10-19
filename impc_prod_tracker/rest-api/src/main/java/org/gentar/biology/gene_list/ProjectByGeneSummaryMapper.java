package org.gentar.biology.gene_list;

import org.gentar.Mapper;
import org.gentar.biology.project.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectByGeneSummaryMapper implements Mapper<Project, ProjectByGeneSummaryDTO>
{
    @Override
    public ProjectByGeneSummaryDTO toDto(Project project)
    {
        ProjectByGeneSummaryDTO projectByGeneSummaryDTO = new ProjectByGeneSummaryDTO();
        projectByGeneSummaryDTO.setTpn(project.getTpn());
        projectByGeneSummaryDTO.setSummaryStatus(project.getSummaryStatus().getName());
        return projectByGeneSummaryDTO;
    }
}
