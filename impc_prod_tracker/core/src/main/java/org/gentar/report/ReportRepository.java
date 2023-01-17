package org.gentar.report;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long> {

    Report findFirstByReportType_NameIsOrderByCreatedAtDesc(String reportName);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="1000")})
    List<Report> findAllByReportType_NameIs(String reportName);

}
