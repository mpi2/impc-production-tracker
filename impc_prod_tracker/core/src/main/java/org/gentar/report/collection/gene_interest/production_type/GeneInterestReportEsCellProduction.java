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
import java.util.Set;
import java.util.stream.Collectors;

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
    private Map<String, String> mutationToAlleleCategory;
    private Map<String, List<String>> projectsForEsCellGenes;
    private Map<String, List<String>> projectsForEsCellNullTargetedGenes;
    private Map<String, List<String>> projectsForEsCellConditionalTargetedGenes;
    private Map<String, String> assignmentForEsCellProjects;

    private Map<String, List<String>> productionPlansForEsCellProjects;
    private Map<String, List<String>> productionPlansForEsCellNullTargetedProjects;
    private Map<String, List<String>> productionPlansForEsCellConditionalTargetedProjects;

    private Map<String, String> statusForEsCellProductionPlans;
    private Map<String, String> statusForEsCellNullProductionPlans;
    private Map<String, String> statusForEsCellConditionalProductionPlans;

    private Map<String, String> summaryAssignmentForEsCellGenes;
    private Map<String, String> summaryProductionPlanStatusForEsCellGenes;
    private Map<String, String> summaryProductionPlanStatusForEsCellNullTargetedGenes;
    private Map<String, String> summaryProductionPlanStatusForEsCellConditionalTargetedGenes;

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
        processMutationTypeProjections();
        mergeProjectionInformation();
        setGeneAssignmentStatuses();
        setGenePlanSummaryStatuses();
    }

    public Set<Long> getESCellOutcomeSet() {
        return esCellGeneProjections
                .stream()
                .map(GeneInterestReportGeneProjection::getOutcomeId)
                .collect(Collectors.toSet());
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

    public Map<String, String> getGeneIdToProductionPlanStatusMapForNullTargetedGenes(){
        return summaryProductionPlanStatusForEsCellNullTargetedGenes;
    }

    public Map<String, String> getGeneIdToProductionPlanStatusMapForConditionalTargetedGenes(){
        return summaryProductionPlanStatusForEsCellConditionalTargetedGenes;
    }


    private void fetchData() {
        esCellProjectProjections = projectService.getGeneInterestReportEsCellProjectProjections();
        esCellGeneProjections = geneService.getGeneInterestReportEsCellGeneProjections();
        esCellMutationTypeProjections = mutationService.getSelectedEsCellMutationTypeProjections(esCellGeneProjections);
    }

    private void processMutationTypeProjections() {
        mutationToAlleleCategory = mutationService.assignAlleleCategories(esCellMutationTypeProjections);
    }


    private void mergeProjectionInformation() {

        esCellGenes = projectionMergeHelper.getGenes(esCellProjectProjections, esCellGeneProjections);

        projectsForEsCellGenes =
                projectionMergeHelper.getProjectsByGene(esCellProjectProjections, esCellGeneProjections);

        assignmentForEsCellProjects =
                projectionMergeHelper.getAssignmentByProject(esCellProjectProjections, esCellGeneProjections);

        productionPlansForEsCellProjects =
                projectionMergeHelper.getPlansByProject(esCellProjectProjections, esCellGeneProjections);

        statusForEsCellProductionPlans =
                projectionMergeHelper.getStatusByPlan(esCellProjectProjections, esCellGeneProjections);


        // Assumption: projects started but without a mutation will be included as null targeted
        projectsForEsCellNullTargetedGenes =
                projectionMergeHelper.getProjectsByGeneFilteredForNullTargeting(
                        esCellProjectProjections, esCellGeneProjections, mutationToAlleleCategory);

//        System.out.println(projectsForEsCellNullTargetedGenes.get("MGI:109331"));
//        Check for Nxn gives:
//        [TPN:000019744, TPN:000026164, TPN:000012405]
//        System.out.println(projectsForEsCellNullTargetedGenes.get("MGI:1914330"));
//        Check for 1700019D03Rik gives:
//        [TPN:000024192]

        productionPlansForEsCellNullTargetedProjects =
                projectionMergeHelper.getPlansByProjectFilteredForNullTargeting(
                        esCellProjectProjections, esCellGeneProjections, mutationToAlleleCategory);

//        System.out.println(productionPlansForEsCellNullTargetedProjects.get("TPN:000024192"));
//        [PIN:0000047184, PIN:0000047183]

        statusForEsCellNullProductionPlans =
                projectionMergeHelper.getStatusByPlanFilteredForNullTargeting(
                        esCellProjectProjections, esCellGeneProjections, mutationToAlleleCategory);

        // Note: Only require esCellGeneProjections as Conditional allele projects will have an associated mutation
        projectsForEsCellConditionalTargetedGenes =
                projectionMergeHelper.getProjectsByGeneFilteredForConditionalTargeting(
                        esCellGeneProjections, mutationToAlleleCategory);

//        System.out.println(projectsForEsCellConditionalTargetedGenes.get("MGI:109331"));
//        Check for Nxn gives:
//        [TPN:000019744, TPN:000012405]
//        System.out.println(projectsForEsCellConditionalTargetedGenes.get("MGI:1914330"));
//        Check for 1700019D03Rik gives:
//        null

        productionPlansForEsCellConditionalTargetedProjects =
                projectionMergeHelper.getPlansByProjectFilteredForConditionalTargeting(
                        esCellGeneProjections, mutationToAlleleCategory);


        statusForEsCellConditionalProductionPlans =
                projectionMergeHelper.getStatusByPlanFilteredForConditionalTargeting(
                        esCellGeneProjections, mutationToAlleleCategory);
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


        summaryProductionPlanStatusForEsCellNullTargetedGenes =
                geneStatusSummaryHelper
                        .calculateGenePlanSummaryStatus(
                                projectsForEsCellNullTargetedGenes,
                                productionPlansForEsCellNullTargetedProjects,
                                statusForEsCellNullProductionPlans);

        summaryProductionPlanStatusForEsCellConditionalTargetedGenes =
                geneStatusSummaryHelper
                        .calculateGenePlanSummaryStatus(
                                projectsForEsCellConditionalTargetedGenes,
                                productionPlansForEsCellConditionalTargetedProjects,
                                statusForEsCellConditionalProductionPlans);

    }


}
