package org.gentar.report.collection.gene_interest.gene;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneInterestReportGeneServiceImpl implements GeneInterestReportGeneService
{
    private final GeneInterestReportGeneRepository geneRepository;

    public GeneInterestReportGeneServiceImpl( GeneInterestReportGeneRepository geneRepository ) {
        this.geneRepository = geneRepository;
    }

    public List<GeneInterestReportGeneProjection> getGeneInterestReportCrisprGeneProjections()
    {
        return geneRepository.findAllGeneInterestReportCrisprGeneProjections();
    }

    public List<GeneInterestReportGeneProjection> getGeneInterestReportEsCellGeneProjections()
    {
        return geneRepository.findAllGeneInterestReportEsCellGeneProjections();
    }
}
