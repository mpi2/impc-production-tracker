package org.gentar.biology.project.specs;

import org.gentar.biology.molecular_mutation_type.MolecularMutationType;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.Gene_;
import org.gentar.biology.molecular_mutation_type.MolecularMutationType_;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.Plan_;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.Project_;
import org.gentar.biology.project.assignment_status.AssignmentStatus;
import org.gentar.biology.project.assignment_status.AssignmentStatus_;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.project.consortium.ProjectConsortium_;
import org.gentar.biology.project.intention.project_intention.ProjectIntention;
import org.gentar.biology.project.intention.project_intention.ProjectIntention_;
import org.gentar.biology.project.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.project.intention.project_intention_gene.ProjectIntentionGene_;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.project.privacy.Privacy_;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.consortium.Consortium_;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_group.WorkGroup_;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnit_;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

public class ProjectPaths
{
    public static Path<String> getTpnPath(Root<Project> root)
    {
        return root.get(Project_.tpn);
    }

    public static Path<String> getExternalReferencePath(Root<Project> root)
    {
        return root.get(Project_.projectExternalRef);
    }

    public static Path<String> getPrivacyNamePath(Root<Project> root)
    {
        Path<Privacy> privacy = root.get(Project_.privacy);
        return privacy.get(Privacy_.name);
    }

    public static Path<String> getAssignmentStatusNamePath(Root<Project> root)
    {
        Path<AssignmentStatus> status = root.get(Project_.assignmentStatus);
        return status.get(AssignmentStatus_.name);
    }

    public static Path<String> getConsortiaNamePath(Root<Project> root)
    {
        Path<ProjectConsortium> projectProjectConsortiumSetJoin =
            root.join(Project_.projectConsortia);
        Path<Consortium> consortiumPath =
            projectProjectConsortiumSetJoin.get(ProjectConsortium_.consortium);
        return consortiumPath.get(Consortium_.name);
    }

    public static Path<String> getWorkUnitNamePath(Root<Project> root)
    {
        SetJoin<Project, Plan> plansJoin = root.join(Project_.plans);
        Path<WorkUnit> workUnitPath = plansJoin.get(Plan_.workUnit);
        return workUnitPath.get(WorkUnit_.name);
    }

    public static Path<String> getWorkGroupNamePath(Root<Project> root)
    {
        SetJoin<Project, Plan> plansJoin = root.join(Project_.plans);
        Path<WorkGroup> workGroupPath = plansJoin.get(Plan_.workGroup);
        return workGroupPath.get(WorkGroup_.name);
    }

    public static Path<String> getMolecularMutationTypeNamePath(Root<Project> root)
    {
        ListJoin<Project, ProjectIntention> projectProjectIntentionSetJoin =
            root.join(Project_.projectIntentions);

        Path<MolecularMutationType> molecularMutationTypePath =
            projectProjectIntentionSetJoin.join(ProjectIntention_.molecularMutationType);

        return molecularMutationTypePath.get(MolecularMutationType_.name);
    }

    public static Path<String> getMarkerSymbolPath(Root<Project> root)
    {
        ListJoin<Project, ProjectIntention> projectProjectIntentionSetJoin =
            root.join(Project_.projectIntentions);

        Path<ProjectIntentionGene> projectIntentionProjectGeneSetJoin =
            projectProjectIntentionSetJoin.get(ProjectIntention_.projectIntentionGene);
        Path<Gene> genePath = projectIntentionProjectGeneSetJoin.get(ProjectIntentionGene_.gene);
        return genePath.get(Gene_.symbol);
    }

    public static Path<String> getAccIdPath(Root<Project> root)
    {
        ListJoin<Project, ProjectIntention> projectProjectIntentionSetJoin =
            root.join(Project_.projectIntentions);
        Path<ProjectIntentionGene> projectGenePath =
            projectProjectIntentionSetJoin.join(ProjectIntention_.projectIntentionGene);
        Path<Gene> genePath = projectGenePath.get(ProjectIntentionGene_.gene);
        return genePath.get(Gene_.accId);
    }
}
