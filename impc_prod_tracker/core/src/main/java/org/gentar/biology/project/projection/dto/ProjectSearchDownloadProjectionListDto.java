package org.gentar.biology.project.projection.dto;

import java.util.List;
import lombok.Data;

/**
 * ProjectProjectionListDto.
 */
@Data
public class ProjectSearchDownloadProjectionListDto {

    List<String> tpn;

    List<String> geneSymbolOrMgi;

    List<String> intention;

    List<String> workUnit;

    List<String> workGroup;

    List<String> projectAssignment;

    List<String> projectSummaryStatus;

    List<String> privacy;

    List<String> consortia;

    List<String> phenotypingExternalRef;

    List<String> productionColonyName;

    public ProjectSearchDownloadProjectionListDto(List<String> nullList) {
        this.tpn = nullList;
        this.geneSymbolOrMgi = nullList;
        this.intention = nullList;
        this.workUnit = nullList;
        this.workGroup = nullList;
        this.projectAssignment = nullList;
        this.projectSummaryStatus = nullList;
        this.privacy = nullList;
        this.consortia = nullList;
        this.phenotypingExternalRef = nullList;
        this.productionColonyName = nullList;
    }
}
