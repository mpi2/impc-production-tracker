package org.gentar.report.collection.mgi_modification_allele.modification_colony;

import org.springframework.beans.factory.annotation.Value;

public interface MgiModificationAlleleReportColonyProjection {


    @Value("#{target.productionPlanId}")
    Long getProductionPlanId();

    @Value("#{target.productionColonyName}")
    String getProductionColonyName();

    @Value("#{target.productionStrainName}")
    String getProductionStrainName();

    @Value("#{target.productionWorkUnit}")
    String getProductionWorkUnit();

    @Value("#{target.productionOutcomeId}")
    Long getProductionOutcomeId();

    @Value("#{target.esCellName}")
    String getEsCellName();

    @Value("#{target.parentalEsCellName}")
    String getParentalEsCellName();

    @Value("#{target.esCellAlleleSymbol}")
    String getEsCellAlleleSymbol();

    @Value("#{target.esCellAlleleAccessionId}")
    String getEsCellAlleleAccessionId();

    @Value("#{target.tatCre}")
    Boolean getTatCre();

    @Value("#{target.deleterStrainName}")
    String getDeleterStrainName();

    @Value("#{target.modificationColonyName}")
    String getModificationColonyName();

    @Value("#{target.modificationStrainName}")
    String getModificationStrainName();

    @Value("#{target.modificationPlanId}")
    Long getModificationPlanId();

    @Value("#{target.modificationWorkUnit}")
    String getModificationWorkUnit();

    @Value("#{target.modificationOutcomeId}")
    Long getModificationOutcomeId();

}
