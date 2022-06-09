package org.gentar.biology.project.descriptor;

import org.gentar.biology.project.projection.dto.ProjectSearchDownloadProjectionDto;
import org.gentar.biology.project.projection.ProjectSearchDownloadProjection;

public class ProjectSearchDownloadMapper {

    private static final String SEPARATOR = ";";

    public static ProjectSearchDownloadProjectionDto projectProjectionToDto(
        ProjectSearchDownloadProjection projectSearchDownloadProjection) {
        ProjectSearchDownloadProjectionDto
            projectSearchDownloadProjectionDto = new ProjectSearchDownloadProjectionDto();
        projectSearchDownloadProjectionDto.setTpn(projectSearchDownloadProjection.getTpn());
        projectSearchDownloadProjectionDto
            .setGeneOrLocation(projectSearchDownloadProjection.getGeneOrLocation());
        projectSearchDownloadProjectionDto.setMgi(projectSearchDownloadProjection.getMgi());
        projectSearchDownloadProjectionDto
            .setMutationIntentions(projectSearchDownloadProjection.getMutationIntentions());
        projectSearchDownloadProjectionDto
            .setWorkUnit(projectSearchDownloadProjection.getWorkUnit());
        projectSearchDownloadProjectionDto
            .setWorkGroup(projectSearchDownloadProjection.getWorkGroup());
        projectSearchDownloadProjectionDto
            .setProjectAssignment(projectSearchDownloadProjection.getProjectAssignment());
        projectSearchDownloadProjectionDto
            .setProjectSummaryStatus(projectSearchDownloadProjection.getProjectSummaryStatus());
        projectSearchDownloadProjectionDto
            .setProductionColonyName(projectSearchDownloadProjection.getColonyName());
        projectSearchDownloadProjectionDto
            .setPhenotypingExternalRef(projectSearchDownloadProjection.getPhenotypingExternalRef());
        projectSearchDownloadProjectionDto.setPrivacy(projectSearchDownloadProjection.getPrivacy());
        projectSearchDownloadProjectionDto
            .setConsortia(projectSearchDownloadProjection.getConsortia());

        return projectSearchDownloadProjectionDto;
    }


    public static String mapOrthologToString(
        ProjectSearchDownloadProjectionDto projectSearchDownloadProjectionDto) {
        String projectProjectionDtoString = "";
        projectProjectionDtoString += projectSearchDownloadProjectionDto.getTpn() + SEPARATOR;
        projectProjectionDtoString +=
            projectSearchDownloadProjectionDto.getGeneOrLocation() + SEPARATOR;
        projectProjectionDtoString += projectSearchDownloadProjectionDto.getMgi() + SEPARATOR;
        projectProjectionDtoString +=
            projectSearchDownloadProjectionDto.getBestOrtholog() + SEPARATOR;
        projectProjectionDtoString +=
            projectSearchDownloadProjectionDto.getMutationIntentions() + SEPARATOR;
        projectProjectionDtoString += projectSearchDownloadProjectionDto.getWorkUnit() + SEPARATOR;
        projectProjectionDtoString += projectSearchDownloadProjectionDto.getWorkGroup() + SEPARATOR;
        projectProjectionDtoString +=
            projectSearchDownloadProjectionDto.getProjectAssignment() + SEPARATOR;
        projectProjectionDtoString +=
            projectSearchDownloadProjectionDto.getProjectSummaryStatus() + SEPARATOR;
        projectProjectionDtoString +=
            projectSearchDownloadProjectionDto.getProductionColonyName() + SEPARATOR;
        projectProjectionDtoString +=
            projectSearchDownloadProjectionDto.getPhenotypingExternalRef() + SEPARATOR;
        projectProjectionDtoString += projectSearchDownloadProjectionDto.getPrivacy() + SEPARATOR;
        projectProjectionDtoString += projectSearchDownloadProjectionDto.getConsortia() + SEPARATOR;

        return projectProjectionDtoString;
    }
}
