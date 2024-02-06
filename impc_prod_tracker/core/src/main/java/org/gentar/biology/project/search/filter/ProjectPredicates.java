package org.gentar.biology.project.search.filter;

import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.project.Project;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProjectPredicates {
    public static Predicate<Project> inTpns(List<String> tpns) {
        return p -> tpns.contains(p.getTpn());
    }

    public static Predicate<Project> inWorkUnitNames(List<String> values) {
        return (p) -> {
            WorkUnit match;
            List<WorkUnit> workUnitsByProject = p.getRelatedWorkUnits();
            match = workUnitsByProject.stream()
                    .filter(wu -> values.contains(wu.getName()))
                    .findAny().orElse(null);
            return match != null;
        };
    }

    public static Predicate<Project> inWorkGroupNames(List<String> values) {
        return (p) -> {
            WorkGroup match;
            List<WorkGroup> workGroupsByProject = p.getRelatedWorkGroups();
            match = workGroupsByProject.stream()
                    .filter(wg -> values.contains(wg.getName()))
                    .findAny().orElse(null);
            return match != null;
        };
    }

    public static Predicate<Project> inMolecularMutationTypeNames(List<String> values) {
        return (p) -> {
            List<MolecularMutationType> molecularMutationTypes = getProjectMolecularMutationTypes(p);
            return molecularMutationTypes.stream()
                    .filter(x -> values.contains(x.getName()))
                    .findFirst().orElse(null) != null;
        };
    }

    private static List<MolecularMutationType> getProjectMolecularMutationTypes(Project project) {
        List<MolecularMutationType> molecularMutationTypes = new ArrayList<>();
        if (project != null) {
            List<ProjectIntention> projectIntentions = project.getProjectIntentions();
            if (projectIntentions != null) {
                projectIntentions.forEach(i -> molecularMutationTypes.add(i.getMolecularMutationType()));
            }
        }
        return molecularMutationTypes;
    }

    public static Predicate<Project> inConsortiaNames(List<String> values) {
        return (p) -> {
            boolean anyMatch;

            var relatedConsortia = p.getRelatedConsortia();
            anyMatch = relatedConsortia.stream()
                    .filter(c -> values.contains(c.getName()))
                    .findFirst().orElse(null) != null;
            return anyMatch;
        };
    }

    public static Predicate<Project> inPrivaciesNames(List<String> values) {
        return p -> values.contains(p.getPrivacy().getName());
    }

    public static Predicate<Project> inImitsMiPlanIds(List<String> values)
    {
        List<Long> ids = new ArrayList<>();
        values.forEach(id -> ids.add(Long.valueOf(id)));

        return p -> ids.contains((p.getImitsMiPlan()));
    }

    public static Predicate<Project> inColonyNames(List<String> values)
    {
        return (p) -> {
            boolean anyMatch;

            var relatedColonies = p.getRelatedColonies();
            anyMatch = relatedColonies.stream()
                    .filter(c -> values.contains(c.getName()))
                    .findFirst().orElse(null) != null;
            return anyMatch;
        };
    }

    public static Predicate<Project> inPhenotypingExternalRefNames(List<String> values)
    {
        return (p) -> {
            boolean anyMatch;

            var relatedPhenotypingAttempt = p.getPlans();
            anyMatch = relatedPhenotypingAttempt.stream()
                    .filter(c -> values.contains(c.getPhenotypingAttempt().getPhenotypingExternalRef()))
                    .findFirst().orElse(null) != null;
            return anyMatch;
        };
    }
}
