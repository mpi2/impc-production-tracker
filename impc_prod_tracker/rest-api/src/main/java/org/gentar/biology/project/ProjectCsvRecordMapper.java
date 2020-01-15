package org.gentar.biology.project;

import org.gentar.Mapper;
import org.gentar.helpers.ProjectCsvRecord;
import org.springframework.stereotype.Component;

@Component
public class ProjectCsvRecordMapper implements Mapper<Project, ProjectCsvRecord>
{
    @Override
    public ProjectCsvRecord toDto(Project project)
    {
        ProjectCsvRecord projectCsvRecord = new ProjectCsvRecord();
        projectCsvRecord.setTpn(project.getTpn());
        return projectCsvRecord;
    }
}
