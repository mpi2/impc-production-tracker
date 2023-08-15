package org.gentar.report.collection.work_unit_attempts.repository;

import java.util.List;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.report.collection.work_unit_attempts.projection.WorkUnitAttemptsProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface WorkUnitAttemptsRepository extends CrudRepository<WorkUnit, Long> {


    @Query(value = "select DISTINCT ON (c.outcome_id) css.date, c.name, m.min, m.symbol AS mutation_symbol, g.symbol AS gene_symbol\n" +
        "from colony c\n" +
        "  JOIN status s ON c.status_id = s.id\n" +
        "  JOIN outcome o ON c.outcome_id = o.id\n" +
        "  JOIN mutation_outcome mo ON o.id = mo.outcome_id\n" +
        "  JOIN mutation m ON mo.mutation_id = m.id\n" +
        "  JOIN mutation_gene mg ON m.id = mg.mutation_id\n" +
        "  JOIN gene g ON mg.gene_id = g.id\n" +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id\n" +
        "  JOIN plan p ON o.plan_id = p.id\n" +
        "  JOIN work_group wg ON p.work_group_id = wg.id\n" +
        "  JOIN work_unit wu ON p.work_unit_id = wu.id\n" +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id\n" +
        "  WHERE s.name in ('Genotype Confirmed', 'Genotype Extinct') AND\n" +
        "  UPPER(a.name) = UPPER(:attempt) AND\n" +
        "  wg.name NOT IN ('EUCOMMToolsCre') AND \n" +
        " UPPER(wu.name) IN (UPPER(:workUnit))\n" +
        "group by c.outcome_id, css.date, c.name, m.min, m.symbol, g.symbol\n" +
        "order by c.outcome_id, css.date, c.name, m.min, m.symbol, g.symbol",
        nativeQuery = true)
    List<WorkUnitAttemptsProjection> findAttemptsByWorkUnitAndAttempt(
        @Param("workUnit") String workUnit,
        @Param("attempt") String attempt);

}

