package org.gentar.report.collection.gene_interest.production_type;

import org.gentar.report.collection.gene_interest.GeneInterestReportProjectionMergeHelperImpl;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneServiceImpl;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectProjection;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectServiceImpl;
import org.gentar.report.utils.assignment.GeneAssignmentStatusHelperImpl;
import org.gentar.report.utils.status.GeneStatusSummaryHelperImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GeneInterestReportCrisprProduction implements GeneInterestReportProductionType{

    private final GeneInterestReportProjectServiceImpl projectService;
    private final GeneInterestReportGeneServiceImpl geneService;
    private final GeneStatusSummaryHelperImpl geneStatusSummaryHelper;
    private final GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper;
    private final GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper;

    private List<GeneInterestReportProjectProjection> crisprProjectProjections;
    private List<GeneInterestReportGeneProjection> crisprGeneProjections;
    private Map<String, String> crisprGenes;
    private Map<String, List<String>> projectsForCrisprGenes;
    private Map<String, String> assignmentForCrisprProjects;

    private Map<String, List<String>> productionPlansForCrisprProjects;
    private Map<String, String> statusForCrisprProductionPlans;
    private Map<String, String> summaryAssignmentForCrisprGenes;
    private Map<String, String> summaryProductionPlanStatusForCrisprGenes;


    public GeneInterestReportCrisprProduction(GeneInterestReportProjectServiceImpl projectService,
                                              GeneInterestReportGeneServiceImpl geneService,
                                              GeneStatusSummaryHelperImpl geneStatusSummaryHelper,
                                              GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper,
                                              GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper)
    {
        this.projectService = projectService;
        this.geneService = geneService;
        this.geneStatusSummaryHelper = geneStatusSummaryHelper;
        this.geneAssignmentStatusHelper = geneAssignmentStatusHelper;
        this.projectionMergeHelper = projectionMergeHelper;
    }

    public void summariseData(){
        fetchData();
        mergeProjectionInformation();
        setGeneAssignmentStatuses();
        setGenePlanSummaryStatuses();
    }

    public Set<Long> getCrisprOutcomeSet() {
        return crisprGeneProjections
                .stream()
                .map(GeneInterestReportGeneProjection::getOutcomeId)
                .collect(Collectors.toSet());
    }

    public Map<String, String> getGeneIdToSymbolMap(){
        return crisprGenes;
    }

    public Map<String, String> getGeneIdToAssignmentMap(){
        return summaryAssignmentForCrisprGenes;
    }

    public Map<String, String> getGeneIdToProductionPlanStatusMap(){
        return summaryProductionPlanStatusForCrisprGenes;
    }

    private void fetchData() {
        crisprProjectProjections = projectService.getGeneInterestReportCrisprProjectProjections();
        crisprGeneProjections = geneService.getGeneInterestReportCrisprGeneProjections();
    }

    private void mergeProjectionInformation() {

        crisprGenes = projectionMergeHelper.getGenes(crisprProjectProjections, crisprGeneProjections);

        projectsForCrisprGenes =
                projectionMergeHelper.getProjectsByGene(crisprProjectProjections, crisprGeneProjections);

        assignmentForCrisprProjects =
                projectionMergeHelper.getAssignmentByProject(crisprProjectProjections, crisprGeneProjections);

        // The production status will be reported by class of project
        productionPlansForCrisprProjects =
                projectionMergeHelper.getPlansByProject(crisprProjectProjections, crisprGeneProjections);

        statusForCrisprProductionPlans =
                projectionMergeHelper.getStatusByPlan(crisprProjectProjections, crisprGeneProjections);
    }

    private void setGeneAssignmentStatuses(){
        summaryAssignmentForCrisprGenes =
                geneAssignmentStatusHelper
                        .calculateGeneAssignmentStatuses(projectsForCrisprGenes, assignmentForCrisprProjects);
    }

    private void setGenePlanSummaryStatuses(){
        summaryProductionPlanStatusForCrisprGenes =
                geneStatusSummaryHelper
                        .calculateGenePlanSummaryStatus(
                                projectsForCrisprGenes,
                                productionPlansForCrisprProjects,
                                statusForCrisprProductionPlans);

    }

}
