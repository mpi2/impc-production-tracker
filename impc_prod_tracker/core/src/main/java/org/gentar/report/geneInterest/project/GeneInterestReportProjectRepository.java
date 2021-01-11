package org.gentar.report.geneInterest.project;

import org.gentar.biology.project.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeneInterestReportProjectRepository extends CrudRepository<Project, Long> {
    @Query("select " +
            "p.id as projectId, " +
            "assign.name as assignmentName, " +
            "assign_stamp.date as assignmentDate, " +
            "g.accId as geneAccId, " +
            "g.symbol as geneSymbol " +
            "FROM Project p " +
            "INNER JOIN Privacy priv on p.privacy=priv " +
            "INNER JOIN AssignmentStatus assign on p.assignmentStatus = assign " +
            "INNER JOIN AssignmentStatusStamp assign_stamp on p.assignmentStatus = assign_stamp.assignmentStatus and p=assign_stamp.project " +
            "INNER JOIN ProjectIntention pi on p = pi.project " +
            "INNER JOIN ProjectIntentionGene pig on pi=pig.projectIntention " +
            "INNER JOIN Gene g on pig.gene=g " +
            "where priv.name='public' ")
    List<GeneInterestReportProjectProjection> findAllGeneInterestReportProjectProjections();
}
