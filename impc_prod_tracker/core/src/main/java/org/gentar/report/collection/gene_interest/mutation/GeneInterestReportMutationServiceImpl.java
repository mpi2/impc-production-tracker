package org.gentar.report.collection.gene_interest.mutation;

import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.utils.mutation.EsCellMutationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
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


    @Override
    public List<GeneInterestReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds){
        return geneInterestReportMutationRepository.findSelectedMutationGeneProjectionsForGeneInterestReport(mutationIds);
    }

    @Override
    public List<GeneInterestReportEsCellMutationTypeProjection> getSelectedEsCellMutationTypeProjections(List<GeneInterestReportGeneProjection> geneProjections) {
        List<String> mutationMins =
                geneProjections
                        .stream()
                        .map(GeneInterestReportGeneProjection::getMutationIdentificationNumber)
                        .collect(Collectors.toList());

        return geneInterestReportMutationRepository.findSelectedEsCellMutationTypeProjectionsForGeneInterestReport(mutationMins);
    }


    public Map<String, String> assignAlleleCategories(List<GeneInterestReportEsCellMutationTypeProjection> mutationTypeProjections ){
        return mutationTypeProjections
                .stream()
                .collect(Collectors.toMap(
                        GeneInterestReportEsCellMutationTypeProjection::getMutationIdentificationNumber,
                        ( i -> classifyMutationType(i.getMutationCategorizationName())),
                        (value1, value2) -> value1 ));
    }

    private String classifyMutationType(String type){
        EsCellMutationType esCellMutationType = EsCellMutationType.valueOfLabel(type);
        if (esCellMutationType != null) {
            return esCellMutationType.getClassification();
        } else {
            return "";
        }
    }

    private static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        return IntStream.iterate(0, i -> i < collection.size(), i -> i + batchSize)
                .mapToObj(i -> collection.subList(i, Math.min(i + batchSize, collection.size())))
                .collect(Collectors.toList());
    }


}
