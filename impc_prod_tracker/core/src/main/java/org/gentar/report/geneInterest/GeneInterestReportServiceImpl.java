package org.gentar.report.geneInterest;

import org.gentar.report.geneInterest.project.GeneInterestReportProjectProjection;
import org.gentar.report.geneInterest.project.GeneInterestReportProjectServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneInterestReportServiceImpl implements GeneInterestReportService
{
    private final GeneInterestReportProjectServiceImpl projectService;

    public GeneInterestReportServiceImpl( GeneInterestReportProjectServiceImpl projectService )
    {
        this.projectService = projectService;
    }

    public void generateGeneInterestReport()
    {
        List<GeneInterestReportProjectProjection> projectProjections = projectService.getGeneInterestReportProjectProjections();
        writeReport(projectProjections);
    }

    private void writeReport(List<GeneInterestReportProjectProjection> pps)
    {
        pps.stream().forEach(x -> System.out.println(
                x.getProjectId() + "\t" +
                x.getAssignmentName() + "\t" +
                x.getAssignmentDate() + "\t" +
                x.getGeneAccId() + "\t" +
                x.getGeneSymbol()));
    }
}
