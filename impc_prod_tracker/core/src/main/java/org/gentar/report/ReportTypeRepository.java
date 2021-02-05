package org.gentar.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportTypeRepository extends CrudRepository<ReportType, Long> {

    ReportType findReportTypeByNameIs(String reportName);

    List<ReportType> findAllByIsPublicIsTrue();

}
