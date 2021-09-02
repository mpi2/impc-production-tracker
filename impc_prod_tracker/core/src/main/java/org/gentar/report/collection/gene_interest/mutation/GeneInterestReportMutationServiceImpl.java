package org.gentar.report.collection.gene_interest.mutation;

import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class GeneInterestReportMutationServiceImpl implements GeneInterestReportMutationService
{

    private final GeneInterestReportMutationRepository geneInterestReportMutationRepository;

    public GeneInterestReportMutationServiceImpl(
            GeneInterestReportMutationRepository geneInterestReportMutationRepository)
    {
        this.geneInterestReportMutationRepository = geneInterestReportMutationRepository;
    }


    public List<GeneInterestReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds){
        return geneInterestReportMutationRepository.findSelectedMutationGeneProjectionsForGeneInterestReport(mutationIds);
    }


    public List<GeneInterestReportEsCellMutationTypeProjection> getSelectedEsCellMutationTypeProjections(
            List<GeneInterestReportGeneProjection> geneProjections)
    {
        List<String> mutationMins =
                geneProjections
                        .stream()
                        .map(i -> i.getMutationIdentificationNumber())
                        .collect(Collectors.toList());

        return geneInterestReportMutationRepository.findSelectedEsCellMutationTypeProjectionsForGeneInterestReport(mutationMins);
    }

    private static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        return IntStream.iterate(0, i -> i < collection.size(), i -> i + batchSize)
                .mapToObj(i -> collection.subList(i, Math.min(i + batchSize, collection.size())))
                .collect(Collectors.toList());
    }

}
