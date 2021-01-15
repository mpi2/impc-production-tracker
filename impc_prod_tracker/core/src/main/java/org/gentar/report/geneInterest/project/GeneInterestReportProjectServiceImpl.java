package org.gentar.report.geneInterest.project;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneInterestReportProjectServiceImpl implements GeneInterestReportProjectService
{

    private final GeneInterestReportProjectRepository projectRepository;

    public GeneInterestReportProjectServiceImpl( GeneInterestReportProjectRepository projectRepository ) {
        this.projectRepository = projectRepository;
    }


    public List<GeneInterestReportProjectProjection> getGeneInterestReportCrisprProjectProjections()
    {
        return projectRepository.findAllGeneInterestReportCrisprProjectProjections();
    }

}
