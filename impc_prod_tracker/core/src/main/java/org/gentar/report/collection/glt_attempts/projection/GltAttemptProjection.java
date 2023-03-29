package org.gentar.report.collection.glt_attempts.projection;

import org.springframework.beans.factory.annotation.Value;

public interface GltAttemptProjection {

    @Value("#{target.year}")
    Long getYear();

    @Value("#{target.month}")
    Long getMonth();

    @Value("#{target.work_unit_name}")
    String getWorkUnitName();

    @Value("#{target.work_group_name}")
    String getWorkGroupName();

    @Value("#{target.sum}")
    Long getSum();

    @Value("#{target.symbol}")
    String getSymbol();

    @Value("#{target.count}")
    Long getCount();
}
