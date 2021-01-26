package org.gentar.report.collection.mgi_crispr_allele.colony;

import org.springframework.beans.factory.annotation.Value;

public interface MgiCrisprAlleleReportColonyProjection {

    @Value("#{target.colonyName}")
    String getColonyName();

    @Value("#{target.strainName}")
    String getStrainName();

    @Value("#{target.productionWorkUnit}")
    String getProductionWorkUnit();

    @Value("#{target.outcomeId}")
    Long getOutcomeId();

}