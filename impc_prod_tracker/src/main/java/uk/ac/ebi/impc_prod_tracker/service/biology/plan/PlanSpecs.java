package uk.ac.ebi.impc_prod_tracker.service.biology.plan;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan_;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project_;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit_;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanSpecs
{
    /**
     * Get all the projects which plans are related with the work units specified in workUnitNames
     * @param workUnitNames List of names of the Work Units
     * @return The found projects. If workUnitNames is null then not filter is applied.
     */
    public static Specification<Plan> withWorkUnitNames(List<String> workUnitNames)
    {
        Specification<Plan> specification;
        if (workUnitNames == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<WorkUnit> workUnitPath = root.get(Plan_.workUnit);
                Path<String> workUnitName = workUnitPath.get(WorkUnit_.name);
                predicates.add(workUnitName.in(workUnitNames));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };

        }
        return specification;
    }

    public static Specification<Plan> withTpns(List<String> tpns)
    {
        Specification<Plan> specification;
        if (tpns == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<Project> projectPath = root.get(Plan_.project);
                Path<String> tpnPath = projectPath.get(Project_.tpn);
                predicates.add(tpnPath.in(tpns));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };

        }
        return specification;
    }

    private static Specification<Plan> buildTrueCondition()
    {
        return (Specification<Plan>) (root, query, criteriaBuilder) ->
            criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
}
