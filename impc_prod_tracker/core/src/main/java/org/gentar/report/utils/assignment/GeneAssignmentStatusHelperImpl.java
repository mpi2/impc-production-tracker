package org.gentar.report.utils.assignment;

import org.gentar.biology.project.assignment.AssignmentStatusServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeneAssignmentStatusHelperImpl implements GeneAssignmentStatusHelper {

    private final AssignmentStatusServiceImpl assignmentStatusService;

    public GeneAssignmentStatusHelperImpl(AssignmentStatusServiceImpl assignmentStatusService) {
        this.assignmentStatusService = assignmentStatusService;
    }


    public Map<String, String> calculateGeneAssignmentStatuses(Map<String, List<String>> projectsForGenes,
                                                               Map<String, String> assignmentForProjects)
    {
        return projectsForGenes
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    return summariseGeneAssignment(e.getValue(), assignmentForProjects);
                }));
    }

    public String summariseGeneAssignment(List<String> projectListForGene, Map<String, String> assignmentForProjects)
    {
        String assignment = "";
        if (projectListForGene.size() == 1){
            assignment = assignmentForProjects.get(projectListForGene.get(0));
        }
        else if (projectListForGene.size() > 1) {
            assignment =
                    assignmentForProjects.get(
                            projectListForGene
                            .stream()
                            .min(compareProjectAssignementByOrdering(assignmentForProjects))
                            .get()
                    );
        }
        return assignment;

    }

    private Comparator<String> compareProjectAssignementByOrdering(Map<String, String> assignmentForProjects) {
        return Comparator.comparing(i -> {
            return assignmentStatusService
                    .getAssignmentStatusOrderingMap()
                    .get(assignmentForProjects.get(i));
        });
    }
}
