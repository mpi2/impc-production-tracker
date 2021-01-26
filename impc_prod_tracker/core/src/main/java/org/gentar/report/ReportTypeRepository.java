package org.gentar.report;

import org.springframework.data.repository.CrudRepository;

public interface ReportTypeRepository extends CrudRepository<ReportType, Long> {

    ReportType findReportTypeByNameIs(String reportName);

}
