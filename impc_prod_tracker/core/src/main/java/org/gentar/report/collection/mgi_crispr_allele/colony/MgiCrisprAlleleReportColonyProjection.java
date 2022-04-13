package org.gentar.report.collection.mgi_crispr_allele.colony;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportColonyProjection {

    @Value("#{target.colonyName}")
    String getColonyName();

    @Value("#{target.genotypingComment}")
    String getGenotypingComment();

    @Value("#{target.strainName}")
    String getStrainName();

    @Value("#{target.planId}")
    Long getPlanId();

    @Value("#{target.productionWorkUnit}")
    String getProductionWorkUnit();

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

}
