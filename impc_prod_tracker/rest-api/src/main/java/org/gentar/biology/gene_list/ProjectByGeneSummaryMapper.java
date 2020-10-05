package org.gentar.biology.gene_list;

import org.gentar.Mapper;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.assignment.AssignmentStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ProjectByGeneSummaryMapper implements Mapper<Project, ProjectByGeneSummaryDTO>
{
    @Override
    public ProjectByGeneSummaryDTO toDto(Project project)
    {
        ProjectByGeneSummaryDTO projectByGeneSummaryDTO = new ProjectByGeneSummaryDTO();
        projectByGeneSummaryDTO.setTpn(project.getTpn());
        projectByGeneSummaryDTO.setSummaryStatus(project.getSummaryStatus().getName());
        AssignmentStatus assignmentStatus = project.getAssignmentStatus();
        if (assignmentStatus != null)
        {
            projectByGeneSummaryDTO.setAssignmentStatusName(assignmentStatus.getName());
        }
        addPlans(projectByGeneSummaryDTO, project);
        return projectByGeneSummaryDTO;
    }

    private void addPlans(ProjectByGeneSummaryDTO projectByGeneSummaryDTO, Project project)
    {
        var plansSet = project.getPlans();
        List<PlanSummaryDTO> planSummaryDTOS = new ArrayList<>();
        plansSet.forEach(x -> {
            PlanSummaryDTO planSummaryDTO = new PlanSummaryDTO();
            planSummaryDTO.setPin(x.getPin());
            planSummaryDTO.setTypeName(x.getPlanType().getName());
            planSummaryDTO.setWorkUnitName(x.getWorkUnit().getName());
            planSummaryDTO.setStatusName(x.getStatus().getName());
            addOutcomes(planSummaryDTO, x);
            planSummaryDTOS.add(planSummaryDTO);
        });
        projectByGeneSummaryDTO.setPlans(planSummaryDTOS);
    }

    private void addOutcomes(PlanSummaryDTO planSummaryDTO, Plan plan)
    {
        List<OutcomeSummaryDTO> outcomeSummaryDTOS = new ArrayList<>();
        var outcomes = plan.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(o -> {
                OutcomeSummaryDTO outcomeSummaryDTO = new OutcomeSummaryDTO();
                outcomeSummaryDTO.setTpo(o.getTpo());
                outcomeSummaryDTO.setMolecularMutationTypeNames(
                    getMolecularMutationTypeNamesByOutcome(o));
                outcomeSummaryDTOS.add(outcomeSummaryDTO);
            });
        }
        planSummaryDTO.setOutcomes(outcomeSummaryDTOS);
    }

    private List<String> getMolecularMutationTypeNamesByOutcome(Outcome outcome)
    {
        List<String> names = new ArrayList<>();
        Set<Mutation> mutations = outcome.getMutations();
        if (mutations != null)
        {
            mutations.forEach(m -> {
                MolecularMutationType molecularMutationType = m.getMolecularMutationType();
                if (molecularMutationType != null)
                {
                    names.add(molecularMutationType.getName());
                }
            });
        }
        return names;
    }
}
