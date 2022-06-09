package org.gentar.biology.project.projection;

import org.springframework.beans.factory.annotation.Value;

/**
 * ProjectProjection.
 */
public interface ProjectSearchDownloadProjection {

    @Value("#{target.tpn}")
    String getTpn();

    @Value("#{target.gene}")
    String getGeneOrLocation();

    @Value("#{target.acc_id}")
    String getMgi();

    @Value("#{target.intention}")
    String getMutationIntentions();

    @Value("#{target.workUnitName}")
    String getWorkUnit();

    @Value("#{target.workGroup}")
    String getWorkGroup();

    @Value("#{target.assignmentStatus}")
    String getProjectAssignment();

    @Value("#{target.summaryStatus}")
    String getProjectSummaryStatus();

    @Value("#{target.phenotypingExternalRef}")
    String getPhenotypingExternalRef();

    @Value("#{target.colonyName}")
    String getColonyName();

    @Value("#{target.privacyName}")
    String getPrivacy();

    @Value("#{target.consortium}")
    String getConsortia();
}
