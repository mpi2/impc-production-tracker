package org.gentar.report.collection.glt_attempts.repository;

import java.sql.Timestamp;
import java.util.List;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.report.collection.glt_attempts.projection.GltAttemptProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface GltAttemptRepository extends CrudRepository<WorkUnit, Long> {


    @Query(value = "with plans as (select DISTINCT " +
        "ON (g.id) css.date,wu.name " +
        "from colony c " +
        "    JOIN status s " +
        "ON c.status_id = s.id " +
        "    JOIN outcome o ON c.outcome_id = o.id " +
        "    JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id " +
        "    JOIN plan p ON o.plan_id = p.id " +
        "    JOIN work_group wg ON p.work_group_id = wg.id " +
        "    JOIN work_unit wu ON p.work_unit_id = wu.id " +
        "    JOIN attempt_type a ON p.attempt_type_id = a.id " +
        "    JOIN mutation_outcome mo ON o.id = mo.outcome_id " +
        "    JOIN mutation m ON mo.mutation_id = m.id " +
        "    JOIN mutation_gene mg ON m.id = mg.mutation_id " +
        "    JOIN gene g ON mg.gene_id = g.id " +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) " +
        "  AND UPPER(a.name) = UPPER(:attempt) " +
        "  AND wg.name NOT IN ('EUCOMMToolsCre') AND " +
        "    UPPER(wu.name) IN (UPPER(:workUnit)) AND" +
        "   css.date BETWEEN :startDate AND :endDate " +
        "order by g.id, css.date), " +
        "    data as ( " +
        "select " +
        "    date_trunc('year', plans.date) as \"year\", " +
        "        plans.name as work_unit_name, " +
        "    count (1) as count " +
        "from plans " +
        "group by year, 1, plans.name) " +
        "select " +
        "    work_unit_name as \"work_unit_name\", " +
        "    EXTRACT(year from year) as \"year\", " +
        "       sum(count)              over (order by year asc rows between unbounded preceding and current row) " +
        "from data",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsByAttemptTypeByWorkUnitWithYear(
        @Param("attempt") String attempt,
        @Param("workUnit") String workUnit,
        @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);

    @Query(value = "with plans as (select DISTINCT " +
        "ON (p.id) css.date, wu.name" +
        "  from colony c" +
        "  JOIN status s ON c.status_id = s.id" +
        "  JOIN outcome o ON c.outcome_id = o.id" +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id" +
        "  JOIN plan p ON o.plan_id = p.id" +
        "  JOIN work_group wg ON p.work_group_id = wg.id" +
        "  JOIN work_unit wu ON p.work_unit_id = wu.id" +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id" +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND" +
        "  UPPER(a.name) = UPPER(:attempt) AND" +
        "  wg.name NOT IN ('EUCOMMToolsCre') AND" +
        "  UPPER(wu.name) IN (UPPER(:workUnit)) AND " +
        "   css.date BETWEEN :startDate AND :endDate " +
        "  order by p.id, css.date)," +
        "  data as (" +
        "    select" +
        "     date_trunc('year', plans.date) as \"year\"," +
        "     date_trunc('month', plans.date) as \"month\"," +
        "    plans.name as work_unit_name," +
        "    count(1) as count" +
        "  from plans" +
        "  group by year, month, 1, plans.name)" +
        "select" +
        "    work_unit_name as \"work_unit_name\"," +
        "  EXTRACT(year from year) as \"year\"," +
        "  EXTRACT(month from month) as \"month\"," +
        "  sum(count) over (order by month, year asc rows between unbounded preceding and current row)" +
        "from data",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsByAttemptTypeByWorkUnitWithMonth(
        @Param("attempt") String attempt,
        @Param("workUnit") String workUnit,
        @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);


    @Query(value = "with plans as (select DISTINCT " +
        "   ON (g.id) css.date,wu.name, wg.name as work_group_name " +
        "   from colony c " +
        "    JOIN status s " +
        "   ON c.status_id = s.id " +
        "    JOIN outcome o ON c.outcome_id = o.id " +
        "    JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id " +
        "    JOIN plan p ON o.plan_id = p.id " +
        "    JOIN work_group wg ON p.work_group_id = wg.id " +
        "    JOIN work_unit wu ON p.work_unit_id = wu.id " +
        "    JOIN attempt_type a ON p.attempt_type_id = a.id " +
        "    JOIN mutation_outcome mo ON o.id = mo.outcome_id " +
        "    JOIN mutation m ON mo.mutation_id = m.id " +
        "    JOIN mutation_gene mg ON m.id = mg.mutation_id " +
        "    JOIN gene g ON mg.gene_id = g.id " +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) " +
        "  AND UPPER(a.name) = UPPER(:attempt) " +
        "  AND wg.name NOT IN ('EUCOMMToolsCre') AND " +
        "    UPPER(wu.name) IN (UPPER(:workUnit)) AND" +
        "    UPPER(wg.name) IN (UPPER(:workGroup)) AND" +
        "   css.date BETWEEN :startDate AND :endDate " +
        "   order by g.id, css.date), " +
        "    data as ( " +
        "   select " +
        "    date_trunc('year', plans.date) as \"year\", " +
        "        plans.name as work_unit_name,     work_group_name, " +
        "    count (1) as count " +
        "   from plans " +
        "   group by year, 1, plans.name,work_group_name) " +
        "   select " +
        "    work_unit_name as \"work_unit_name\",work_group_name, " +
        "    EXTRACT(year from year) as \"year\", " +
        "       sum(count)              over (order by year asc rows between unbounded preceding and current row) " +
        "   from data",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithYear(
        @Param("attempt") String attempt,
        @Param("workUnit") String workUnit,
        @Param("workGroup") String workGroup,
        @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);

    @Query(value = "with plans as (select DISTINCT " +
        "ON (p.id) css.date, wu.name, wg.name as work_group_name" +
        "  from colony c" +
        "  JOIN status s ON c.status_id = s.id" +
        "  JOIN outcome o ON c.outcome_id = o.id" +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id" +
        "  JOIN plan p ON o.plan_id = p.id" +
        "  JOIN work_group wg ON p.work_group_id = wg.id" +
        "  JOIN work_unit wu ON p.work_unit_id = wu.id" +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id" +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND" +
        "  UPPER(a.name) = UPPER(:attempt) AND" +
        "  wg.name NOT IN ('EUCOMMToolsCre') AND" +
        "  UPPER(wu.name) IN (UPPER(:workUnit)) AND " +
        "   UPPER(wg.name) IN (UPPER(:workGroup)) AND" +
        "   css.date BETWEEN :startDate AND :endDate " +
        "  order by p.id, css.date)," +
        "  data as (" +
        "    select" +
        "     date_trunc('year', plans.date) as \"year\"," +
        "     date_trunc('month', plans.date) as \"month\"," +
        "    plans.name as work_unit_name, work_group_name," +
        "    count(1) as count" +
        "  from plans" +
        "  group by year, month, 1, plans.name, work_group_name)" +
        "   select" +
        "    work_unit_name as \"work_unit_name\", work_group_name," +
        "  EXTRACT(year from year) as \"year\"," +
        "  EXTRACT(month from month) as \"month\"," +
        "  sum(count) over (order by month, year asc rows between unbounded preceding and current row)" +
        "   from data",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsByAttemptTypeByWorkUnitWorkGroupWithMonth(
        @Param("attempt") String attempt,
        @Param("workUnit") String workUnit,
        @Param("workGroup") String workGroup,
        @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);


    @Query(value = "with plans as (select DISTINCT " +
        "ON (g.id) css.date" +
        "   from colony c" +
        "  JOIN status s ON c.status_id = s.id" +
        "  JOIN outcome o ON c.outcome_id = o.id" +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id" +
        "  JOIN plan p ON o.plan_id = p.id" +
        "  JOIN work_group wg ON p.work_group_id = wg.id" +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id" +
        "  JOIN mutation_outcome mo ON o.id = mo.outcome_id" +
        "  JOIN mutation m ON mo.mutation_id = m.id" +
        "  JOIN mutation_gene mg ON m.id = mg.mutation_id" +
        "  JOIN gene g ON mg.gene_id = g.id" +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND" +
        "  UPPER(a.name) = UPPER(:attempt) AND" +
        "  wg.name NOT IN ('EUCOMMToolsCre') AND " +
        "   css.date BETWEEN :startDate AND :endDate " +
        "   order by g.id, css.date)," +
        "   data as (" +
        "    select" +
        "     date_trunc('year', plans.date) as \"year\"," +
        "    count(1) as count" +
        "  from plans" +
        "  group by year, 1)" +
        "   select" +
        "  EXTRACT(year from year) as \"year\"," +
        "  sum(count) over (order by year asc rows between unbounded preceding and current row)" +
        "   from data;",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsByAttemptTypeWithYear(
        @Param("attempt") String attempt,
        @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);


    @Query(value = "with plans as (select DISTINCT" +
        "   ON (g.id) css.date,wu.name" +
        "   from colony c" +
        "    JOIN status s" +
        "   ON c.status_id = s.id" +
        "    JOIN outcome o ON c.outcome_id = o.id" +
        "    JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id" +
        "    JOIN plan p ON o.plan_id = p.id" +
        "    JOIN work_group wg ON p.work_group_id = wg.id" +
        "    JOIN work_unit wu ON p.work_unit_id = wu.id" +
        "    JOIN attempt_type a ON p.attempt_type_id = a.id" +
        "    JOIN mutation_outcome mo ON o.id = mo.outcome_id" +
        "    JOIN mutation m ON mo.mutation_id = m.id" +
        "    JOIN mutation_gene mg ON m.id = mg.mutation_id" +
        "    JOIN gene g ON mg.gene_id = g.id" +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND" +
        "  UPPER(a.name) = UPPER(:attempt) AND" +
        "    wg.name NOT IN ('EUCOMMToolsCre') AND" +
        "   css.date BETWEEN :startDate AND :endDate " +
        "   order by g.id, css.date)," +
        "    data as (" +
        "    select" +
        "     date_trunc('year', plans.date) as \"year\"," +
        "     date_trunc('month', plans.date) as \"month\"," +
        "    count(1) as count" +
        "  from plans" +
        "  group by year, month, 1)" +
        "   select" +
        "  EXTRACT(year from year) as \"year\"," +
        "  EXTRACT(month from month) as \"month\"," +
        "  sum(count) over (order by month, year asc rows between unbounded preceding and current row)" +
        "   from data",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsByAttemptTypeWithMonth(
        @Param("attempt") String attempt,
        @Param("startDate") Timestamp startDate,
        @Param("endDate") Timestamp endDate);



    @Query(value = "with crispr_glt as ( " +
        "select g.symbol as symbol " +
        "from colony c " +
        "  JOIN status s ON c.status_id = s.id " +
        "  JOIN outcome o ON c.outcome_id = o.id " +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id " +
        "  JOIN plan p ON o.plan_id = p.id " +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id " +
        "  JOIN mutation_outcome mo ON o.id = mo.outcome_id " +
        "  JOIN mutation m ON mo.mutation_id = m.id " +
        "  JOIN mutation_gene mg ON m.id = mg.mutation_id " +
        "  JOIN gene g ON mg.gene_id = g.id " +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND " +
        "  a.name = 'crispr' " +
        " group by g.symbol " +
        " order by g.symbol), " +
        " es_glt as ( " +
        " select  g.symbol as symbol " +
        " from colony c " +
        "  JOIN status s ON c.status_id = s.id " +
        "  JOIN outcome o ON c.outcome_id = o.id " +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id " +
        "  JOIN plan p ON o.plan_id = p.id " +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id " +
        "  JOIN work_group wg ON p.work_group_id = wg.id " +
        "  JOIN mutation_outcome mo ON o.id = mo.outcome_id " +
        "  JOIN mutation m ON mo.mutation_id = m.id " +
        "  JOIN mutation_gene mg ON m.id = mg.mutation_id " +
        "  JOIN gene g ON mg.gene_id = g.id " +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND " +
        "  a.name = 'es cell' AND " +
        "  wg.name != 'EUCOMMToolsCre' " +
        " group by g.symbol " +
        " order by g.symbol), " +
        " glt_intersection as ( " +
        "  select symbol from crispr_glt intersect select symbol from es_glt order by symbol) " +
        " select symbol from glt_intersection;",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsIntersection();



    @Query(value = "with crispr_glt as ( " +
        " select g.symbol as symbol " +
        " from colony c " +
        "  JOIN status s ON c.status_id = s.id " +
        "  JOIN outcome o ON c.outcome_id = o.id " +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id " +
        "  JOIN plan p ON o.plan_id = p.id " +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id " +
        "  JOIN mutation_outcome mo ON o.id = mo.outcome_id " +
        "  JOIN mutation m ON mo.mutation_id = m.id " +
        "  JOIN mutation_gene mg ON m.id = mg.mutation_id " +
        "  JOIN gene g ON mg.gene_id = g.id " +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND " +
        "  a.name = 'crispr' " +
        " group by g.symbol " +
        " order by g.symbol), " +
        " es_glt as ( " +
        " select  g.symbol as symbol " +
        " from colony c " +
        "  JOIN status s ON c.status_id = s.id " +
        "  JOIN outcome o ON c.outcome_id = o.id " +
        "  JOIN colony_status_stamp css ON c.outcome_id = css.colony_id and c.status_id = css.status_id " +
        "  JOIN plan p ON o.plan_id = p.id " +
        "  JOIN attempt_type a ON p.attempt_type_id = a.id " +
        "  JOIN work_group wg ON p.work_group_id = wg.id " +
        "  JOIN mutation_outcome mo ON o.id = mo.outcome_id " +
        "  JOIN mutation m ON mo.mutation_id = m.id " +
        "  JOIN mutation_gene mg ON m.id = mg.mutation_id " +
        "  JOIN gene g ON mg.gene_id = g.id " +
        "  WHERE s.name IN ('Genotype Confirmed', 'Genotype Extinct' ) AND " +
        "  a.name = 'es cell' AND " +
        "  wg.name != 'EUCOMMToolsCre' " +
        " group by g.symbol " +
        " order by g.symbol), " +
        " glt_union as ( " +
        "  select distinct(symbol) as symbol from crispr_glt union select symbol from es_glt order by symbol) " +
        " select count(symbol) from glt_union;",
        nativeQuery = true)
    List<GltAttemptProjection> findGltAttemptsUnion();
}

