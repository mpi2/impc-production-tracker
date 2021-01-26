package org.gentar.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long> {

    Report findFirstByReportType_NameIsOrderByCreatedAtDesc(String reportName);

    List<Report> findAllByReportType_NameIs(String reportName);

}
