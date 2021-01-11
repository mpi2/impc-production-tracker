package org.gentar.biology.gene_list.record;

import org.springframework.beans.factory.annotation.Value;

public interface GeneListProjection {

    @Value("#{target.id}")
    Long getId();

    @Value("#{target.note}")
    String getNote();

    @Value("#{target.visible}")
    String getVisible();

    @Value("#{target.symbol}")
    String getSymbol();

    @Value("#{target.accId}")
    String getAccId();

    @Value("#{target.tpn}")
    String getTpn();

    @Value("#{target.assignmentStatus}")
    String getAssignmentStatus();

    @Value("#{target.privacy}")
    String getPrivacy();

    @Value("#{target.summaryStatus}")
    String getSummaryStatus();
}
