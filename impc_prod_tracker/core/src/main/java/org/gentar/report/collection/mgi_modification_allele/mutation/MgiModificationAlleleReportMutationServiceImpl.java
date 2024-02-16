package org.gentar.report.collection.mgi_modification_allele.mutation;

import org.gentar.report.utils.mutation.EsCellModificationAlleleReportMutationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MgiModificationAlleleReportMutationServiceImpl implements MgiModificationAlleleReportMutationService{
    private final MgiModificationAlleleReportMutationRepository modificationAlleleReportMutationRepository;

    MgiModificationAlleleReportMutationServiceImpl(MgiModificationAlleleReportMutationRepository modificationAlleleReportMutationRepository) {
        this.modificationAlleleReportMutationRepository = modificationAlleleReportMutationRepository;
    }

    @Override
    public List<MgiModificationAlleleReportMutationGeneProjection> getSelectedMutationGeneProjections(List mutationIds) {
        return modificationAlleleReportMutationRepository.findSelectedMutationGeneProjections(mutationIds);
    }

    @Override
    public List<MgiModificationAlleleReportEsCellMutationTypeProjection> getSelectedEsCellMutationTypeProjections(
            List<MgiModificationAlleleReportMutationGeneProjection> mutationGeneProjections) {
        List<String> mutationMins =
                mutationGeneProjections
                        .stream()
                        .map(MgiModificationAlleleReportMutationGeneProjection::getMutationIdentificationNumber)
                        .collect(Collectors.toList());

        return modificationAlleleReportMutationRepository.findSelectedEsCellMutationTypeProjections(mutationMins);
    }


    public Map<Long, String> assignAlleleCategories(List<MgiModificationAlleleReportEsCellMutationTypeProjection> mutationTypeProjections ){
        return mutationTypeProjections
                .stream()
                .collect(Collectors.toMap(
                        MgiModificationAlleleReportEsCellMutationTypeProjection::getMutationId,
                        ( i -> classifyMutationType(i.getMutationCategorizationName())),
                        (value1, value2) -> value1 ));
    }

    private String classifyMutationType(String type){
        EsCellModificationAlleleReportMutationType esCellMutationType =
                EsCellModificationAlleleReportMutationType.valueOfLabel(type);
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
