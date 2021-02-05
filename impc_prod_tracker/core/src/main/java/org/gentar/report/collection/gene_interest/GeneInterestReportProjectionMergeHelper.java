package org.gentar.report.collection.gene_interest;

import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectProjection;

import java.util.List;
import java.util.Map;

public interface GeneInterestReportProjectionMergeHelper {


    /* This helper provides methods to merge the information in two set of projections
    *
    *  projects associated with a gene intention.
    *        This includes projects were a crispr attempt has NOT been reported.
    *
    *  genes associated with mutations and outcomes
    *        This includes genes that may not have been specified as the intention.
    */



    /**
     *
     * @param pps List of Spring GeneInterestReportProjectProjection database projections
     * @param gps List of Spring GeneInterestReportGeneProjection database projections
     * @return a map containing:
     *              Key: the MGI gene accession ID
     *              Value: the Gene Symbol
     */
    Map<String, String> getGenes (List<GeneInterestReportProjectProjection> pps,
                                  List<GeneInterestReportGeneProjection> gps);



    /**
     *
     * @param pps List of Spring GeneInterestReportProjectProjection database projections
     * @param gps List of Spring GeneInterestReportGeneProjection database projections
     * @return a map containing:
     *              Key: the Plan PIN ID
     *              Value: the Plan summary status
     */
    Map<String, String> getStatusByPlan(List<GeneInterestReportProjectProjection> pps,
                                        List<GeneInterestReportGeneProjection> gps);


    /**
     *
     * @param pps List of Spring GeneInterestReportProjectProjection database projections
     * @param gps List of Spring GeneInterestReportGeneProjection database projections
     * @return a map containing:
     *              Key: the MGI gene accession ID
     *              Value: a list of Project TPN IDs associated with the gene
     */
    Map<String, List<String>> getProjectsByGene(List<GeneInterestReportProjectProjection> pps,
                                                List<GeneInterestReportGeneProjection> gps);


    /**
     *
     * @param pps List of Spring GeneInterestReportProjectProjection database projections
     * @param gps List of Spring GeneInterestReportGeneProjection database projections
     * @return a map containing:
     *              Key: Project TPN ID
     *              Value: List of Plan PIN IDs associated with the project
     */
    Map<String, List<String>> getPlansByProject(List<GeneInterestReportProjectProjection> pps,
                                                List<GeneInterestReportGeneProjection> gps);


    /**
     *
     * @param pps List of Spring GeneInterestReportProjectProjection database projections
     * @param gps List of Spring GeneInterestReportGeneProjection database projections
     * @return a map containing:
     *              Key: the Project TPN ID
     *              Value: the Project Assignment status
     */
    Map<String, String> getAssignmentByProject(List<GeneInterestReportProjectProjection> pps,
                                               List<GeneInterestReportGeneProjection> gps);
}
