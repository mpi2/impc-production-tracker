package org.gentar.biology.project;

import java.util.List;
import org.gentar.biology.project.projection.ProjectSearchDownloadProjection;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadProjectionListDto;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@Primary
public interface ProjectRepository extends
    PagingAndSortingRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    @Query("SELECT max(p.tpn) FROM Project p")
    String getMaxTpn();

    @Query(value = "SELECT pr.tpn as tpn,\n" +
        "           g.symbol as gene,\n" +
        "           g.acc_id as acc_id,\n" +
        "           mmt.name as intention,\n" +
        "           string_agg(DISTINCT wu.name, ':')  as workUnitName,\n" +
        "           string_agg(DISTINCT wg.name, ':') as workGroup,\n" +
        "           ast.name as assignmentStatus,\n" +
        "           s.name as summaryStatus,\n" +
        "           prv.name as privacyName,\n" +
        "           string_agg(DISTINCT pa.phenotyping_external_ref, ':') as phenotypingExternalRef,\n" +
        "           string_agg(DISTINCT  co.name, ':') as colonyName,\n" +
        "           string_agg(DISTINCT c.name, ':') as consortium\n" +
        "                            FROM Project pr LEFT JOIN project_intention pi\n" +
        "                            on pr.id = pi.project_id\n" +
        "                                left Join project_intention_gene pig on pi.id = pig.project_intention_id\n" +
        "                                Left Join gene g on pig.gene_id = g.id\n" +
        "                                Left Join molecular_mutation_type mmt on pi.molecular_mutation_type_id = mmt.id\n" +
        "                                left Join plan p on pr.id = p.project_id\n" +
        "                                left Join outcome o on o.plan_id=p.id\n" +
        "                                left Join colony co on co.outcome_id =o.id\n" +
        "                                left Join phenotyping_attempt pa on p.id = pa.plan_id\n" +
        "                                left join work_unit wu on p.work_unit_id = wu.id\n" +
        "                                left Join work_group wg on p.work_group_id = wg.id\n" +
        "                                left join assignment_status ast on pr.assignment_status_id = ast.id\n" +
        "                                left join status s on pr.summary_status_id = s.id\n" +
        "                                left join privacy prv on pr.privacy_id = prv.id\n" +
        "                                left join project_consortium pc on pr.id = pc.project_id\n" +
        "                                left join consortium c on pc.consortium_id = c.id" +
        "                            where   (:#{#filterString.getTpn().get(0)}='null' or pr.tpn IN :#{#filterString.getTpn()}) " +
        "                                AND (:#{#filterString.getIntention().get(0)}='null' or mmt.name IN :#{#filterString.getIntention()}) " +
        "                                AND  (:#{#filterString.getGeneSymbolOrMgi().get(0)}='null'  or g.acc_id IN :#{#filterString.getGeneSymbolOrMgi()} or g.symbol IN :#{#filterString.getGeneSymbolOrMgi()})" +
        "                                AND (:#{#filterString.getWorkUnit().get(0)}='null' or wu.name IN :#{#filterString.getWorkUnit()}) " +
        "                                AND (:#{#filterString.getWorkGroup().get(0)}='null' or wg.name IN :#{#filterString.getWorkGroup()}) " +
        "                                AND (:#{#filterString.getPrivacy().get(0)}='null' or prv.name IN :#{#filterString.getPrivacy()}) " +
        "                                AND (:#{#filterString.getProjectSummaryStatus().get(0)}='null' or s.name IN :#{#filterString.getProjectSummaryStatus()}) " +
        "                                AND (:#{#filterString.getConsortia().get(0)}='null' or c.name IN :#{#filterString.getConsortia()}) " +
        "                                AND (:#{#filterString.getPhenotypingExternalRef().get(0)}='null' or pa.phenotyping_external_ref IN :#{#filterString.getPhenotypingExternalRef()}) " +
        "                                AND (:#{#filterString.getProductionColonyName().get(0)}='null' or co.name IN :#{#filterString.getProductionColonyName()}) " +
        "  group by pr.tpn, g.symbol, g.acc_id, mmt.name, ast.name, prv.name, s.name",
        nativeQuery = true)
    List<ProjectSearchDownloadProjection> findAllProjectsForCsvFile(
        @Param("filterString") ProjectSearchDownloadProjectionListDto filterString);


    Project findByTpn(String tpn);
}
