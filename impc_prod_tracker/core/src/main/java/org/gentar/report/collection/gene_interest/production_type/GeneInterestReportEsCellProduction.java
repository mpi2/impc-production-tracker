package org.gentar.report.collection.gene_interest.production_type;

import org.gentar.report.collection.gene_interest.GeneInterestReportProjectionMergeHelperImpl;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneServiceImpl;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportEsCellMutationTypeProjection;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationServiceImpl;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectProjection;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectServiceImpl;
import org.gentar.report.utils.assignment.GeneAssignmentStatusHelperImpl;
import org.gentar.report.utils.status.GeneStatusSummaryHelperImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class GeneInterestReportEsCellProduction implements GeneInterestReportProductionType{

    private final GeneInterestReportProjectServiceImpl projectService;
    private final GeneInterestReportGeneServiceImpl geneService;
    private final GeneInterestReportMutationServiceImpl mutationService;
    private final GeneStatusSummaryHelperImpl geneStatusSummaryHelper;
    private final GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper;
    private final GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper;

    private List<GeneInterestReportProjectProjection> esCellProjectProjections;
    private List<GeneInterestReportGeneProjection> esCellGeneProjections;
    private List<GeneInterestReportEsCellMutationTypeProjection> esCellMutationTypeProjections;

    private Map<String, String> esCellGenes;
    private Map<String, List<String>> projectsForEsCellGenes;
    private Map<String, String> assignmentForEsCellProjects;

    private Map<String, List<String>> productionPlansForEsCellProjects;
    private Map<String, String> statusForEsCellProductionPlans;
    private Map<String, String> summaryAssignmentForEsCellGenes;
    private Map<String, String> summaryProductionPlanStatusForEsCellGenes;

    public GeneInterestReportEsCellProduction(GeneInterestReportProjectServiceImpl projectService,
                                              GeneInterestReportGeneServiceImpl geneService,
                                              GeneInterestReportMutationServiceImpl mutationService,
                                              GeneStatusSummaryHelperImpl geneStatusSummaryHelper,
                                              GeneAssignmentStatusHelperImpl geneAssignmentStatusHelper,
                                              GeneInterestReportProjectionMergeHelperImpl projectionMergeHelper)
    {
        this.projectService = projectService;
        this.geneService = geneService;
        this.mutationService = mutationService;
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

    public Map<String, String> getGeneIdToSymbolMap(){
        return esCellGenes;
    }

    public Map<String, String> getGeneIdToAssignmentMap(){
        return summaryAssignmentForEsCellGenes;
    }

    public Map<String, String> getGeneIdToProductionPlanStatusMap(){
        return summaryProductionPlanStatusForEsCellGenes;
    }


    private void fetchData() {
        esCellProjectProjections = projectService.getGeneInterestReportEsCellProjectProjections();
        esCellGeneProjections = geneService.getGeneInterestReportEsCellGeneProjections();
        esCellMutationTypeProjections = mutationService.getSelectedEsCellMutationTypeProjections(esCellGeneProjections);
    }


    private void mergeProjectionInformation() {

        esCellGenes = projectionMergeHelper.getGenes(esCellProjectProjections, esCellGeneProjections);

        projectsForEsCellGenes =
                projectionMergeHelper.getProjectsByGene(esCellProjectProjections, esCellGeneProjections);

        assignmentForEsCellProjects =
                projectionMergeHelper.getAssignmentByProject(esCellProjectProjections, esCellGeneProjections);

        // The production status will be reported by class of project
        productionPlansForEsCellProjects =
                projectionMergeHelper.getPlansByProject(esCellProjectProjections, esCellGeneProjections);

        statusForEsCellProductionPlans =
                projectionMergeHelper.getStatusByPlan(esCellProjectProjections, esCellGeneProjections);
    }

    private void setGeneAssignmentStatuses(){
        summaryAssignmentForEsCellGenes =
                geneAssignmentStatusHelper
                        .calculateGeneAssignmentStatuses(projectsForEsCellGenes, assignmentForEsCellProjects);
    }

    private void setGenePlanSummaryStatuses(){
        summaryProductionPlanStatusForEsCellGenes =
                geneStatusSummaryHelper
                        .calculateGenePlanSummaryStatus(
                                projectsForEsCellGenes,
                                productionPlansForEsCellProjects,
                                statusForEsCellProductionPlans);

    }


}
