/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.plan;

import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptType_;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt_;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt_;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanType_;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.Status_;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_group.WorkGroup_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.Project_;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnit_;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanSpecs
{
    /**
     * Get all the plans that are related with the work units specified in workUnitNames
     * @param workUnitNames List of names of the Work Units
     * @return The found plans. If workUnitNames is null then not filter is applied.
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

    /**
     * Get all the plans that are related with the project tpns specified in ProjectTpns
     * @param ProjectTpns List of names of the tpns
     * @return The found plans. If ProjectTpns is null then not filter is applied.
     */
    public static Specification<Plan> withProjectTpns(List<String> ProjectTpns)
    {
        Specification<Plan> specification;
        if (ProjectTpns == null)
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
                predicates.add(tpnPath.in(ProjectTpns));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };

        }
        return specification;
    }

    /**
     * Get all the plans that are related with the work groups specified in workUnitNames
     * @param workGroupNames List of names of the Work Groups
     * @return The found plans. If workGroupNames is null then not filter is applied.
     */
    public static Specification<Plan> withWorkGroupNames(List<String> workGroupNames)
    {
        Specification<Plan> specification;
        if (workGroupNames == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<WorkGroup> workGroupPath = root.get(Plan_.workGroup);
                Path<String> workGroupName = workGroupPath.get(WorkGroup_.name);
                predicates.add(workGroupName.in(workGroupNames));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };

        }
        return specification;
    }

    /**
     * Get all the plans that have the indicated statuses.
     * @param statusNames List of statuses.
     * @return The found plans. If statusNames is null then not filter is applied.
     */
    public static Specification<Plan> withStatusNames (List<String> statusNames)
    {
        Specification<Plan> specification;
        if (statusNames == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<Status> summaryStatus = root.get(Plan_.status);
                Path<String> statusSummaryNamePath = summaryStatus.get(Status_.name);
                predicates.add(statusSummaryNamePath.in(statusNames));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the plans that have the indicated summary statuses.
     * @param summaryStatusNames List of summary statuses.
     * @return The found plans. If summaryStatusNames is null then not filter is applied.
     */
    public static Specification<Plan> withSummaryStatusNames (List<String> summaryStatusNames)
    {
        Specification<Plan> specification;
        if (summaryStatusNames == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<Status> summaryStatus = root.get(Plan_.summaryStatus);
                Path<String> statusSummaryNamePath = summaryStatus.get(Status_.name);
                predicates.add(statusSummaryNamePath.in(summaryStatusNames));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the plans that are related with the pin specified in pins
     * @param pins List of ids of the Pins
     * @return The found plans. If pins is null then not filter is applied.
     */
    public static Specification<Plan> withPins (List<String> pins)
    {
        Specification<Plan> specification;
        if (pins == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<String> pinPath = root.get(Plan_.pin);
                predicates.add(pinPath.in(pins));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the plans that are related with the plan types specified in typeNames
     * @param typeNames List of names of the Plan Types
     * @return The found plans. If typeNames is null then not filter is applied.
     */
    public static Specification<Plan> withTypeNames (List<String> typeNames)
    {
        Specification<Plan> specification;
        if (typeNames == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<PlanType> planTypePath = root.get(Plan_.planType);
                Path<String> planTypeNamePath = planTypePath.get(PlanType_.name);
                predicates.add(planTypeNamePath.in(typeNames));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the plans that are related with the attempt type names specified in attemptTypeNames
     * @param attemptTypeNames List of names of the Attempt Types
     * @return The found plans. If attemptTypeNames is null then not filter is applied.
     */
    public static Specification<Plan> withAttemptTypeNames (List<String> attemptTypeNames)
    {
        Specification<Plan> specification;
        if (attemptTypeNames == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<AttemptType> attemptTypePath = root.get(Plan_.attemptType);
                Path<String> attemptTypeNamePath = attemptTypePath.get(AttemptType_.name);
                predicates.add(attemptTypeNamePath.in(attemptTypeNames));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the plans that are related with the imits mi_attempts specified in imitsMiAttempts
     * @param imitsMiAttempts List of ids in iMits
     * @return The found plans. If imitsMiAttempts is null then not filter is applied.
     */
    public static Specification<Plan> withImitsMiAttempts (List<String> imitsMiAttempts)
    {
        Specification<Plan> specification;
        if (imitsMiAttempts == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<CrisprAttempt> crisprAttemptPath = root.get(Plan_.crisprAttempt);
                Path<Long> imitsMiAttemptPath = crisprAttemptPath.get(CrisprAttempt_.imitsMiAttempt);
                predicates.add(imitsMiAttemptPath.in(imitsMiAttempts));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the plans that are related with the imits phenotyping attempts specified in imitsPhenotypeAttempts
     * @param imitsPhenotypeAttempts List of ids in iMits
     * @return The found plans. If imitsPhenotypeAttempts is null then not filter is applied.
     */
    public static Specification<Plan> withImitsPhenotypeAttempts (List<String> imitsPhenotypeAttempts)
    {
        Specification<Plan> specification;
        if (imitsPhenotypeAttempts == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<PhenotypingAttempt> phenotypingAttemptPath = root.get(Plan_.phenotypingAttempt);
                Path<Long> imitsPhenotypingAttemptPath = phenotypingAttemptPath.get(PhenotypingAttempt_.imitsPhenotypeAttempt);
                predicates.add(imitsPhenotypingAttemptPath.in(imitsPhenotypeAttempts));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    /**
     * Get all the plans that are related with the work groups specified in phenotypingExternalRefs
     * @param phenotypingExternalRefs List of names of the Work Groups
     * @return The found plans. If imitsPhenotypeAttempts is null then not filter is applied.
     */
    public static Specification<Plan> withPhenotypingExternalRefs (List<String> phenotypingExternalRefs)
    {
        Specification<Plan> specification;
        if (phenotypingExternalRefs == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<PhenotypingAttempt> phenotypingAttemptPath = root.get(Plan_.phenotypingAttempt);
                Path<String> phenotypingExternalRefPath = phenotypingAttemptPath.get(PhenotypingAttempt_.phenotypingExternalRef);
                predicates.add(phenotypingExternalRefPath.in(phenotypingExternalRefs));
                query.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            };
        }
        return specification;
    }

    public static Specification<Plan> withDoNotCountTowardsCompleteness (List<String> doNotCountTowardsCompleteness)
    {
        Specification<Plan> specification;
        if (doNotCountTowardsCompleteness == null)
        {
            specification = buildTrueCondition();
        }
        else
        {
            specification = (Specification<Plan>) (root, query, criteriaBuilder) ->
            {
                List<Predicate> predicates = new ArrayList<>();
                Path<PhenotypingAttempt> phenotypingAttemptPath = root.get(Plan_.phenotypingAttempt);
                Path<Boolean> doNotCountTowardsCompletenessPath = phenotypingAttemptPath.get(
                        PhenotypingAttempt_.doNotCountTowardsCompleteness);
                Boolean value = "true".equalsIgnoreCase(doNotCountTowardsCompleteness.get(0)) ? Boolean.TRUE :
                        "false".equalsIgnoreCase(doNotCountTowardsCompleteness.get(0)) ? Boolean.FALSE : null;
                predicates.add(doNotCountTowardsCompletenessPath.in(value));
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
