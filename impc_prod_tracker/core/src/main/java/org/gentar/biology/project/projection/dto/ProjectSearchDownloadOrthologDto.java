package org.gentar.biology.project.projection.dto;

import lombok.Data;

@Data
public class ProjectSearchDownloadOrthologDto {

    String humanGeneSymbol;

    String hgncAccId;

    Long humanSupportCountThreshold;

    String humanCategoryForThreshold;

    Long humanOrthologsAboveThreshold;

    String category;

    Long supportCount;

    String isMaxHumanToMouse;

    String isMaxMouseToHuman;

    Long mouseOrthologsAboveThreshold;

    String mouseCategoryForThreshold;

    Long mouseSupportCountThreshold;

    String mgiGeneAccId;

    String mouseGeneSymbol;
}
