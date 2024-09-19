package org.gentar.biology.mutation;

import java.io.IOException;
import java.util.*;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.mutation.mgi.MgiAlleleDto;
import org.gentar.biology.mutation.mgi.MgiAlleleResponseDto;
import org.gentar.biology.mutation.mutation_ensembl.CombinedMutationEnsemblDto;
import org.gentar.biology.mutation.mutation_ensembl.MutationEnsemblEnsemblPartDto;
import org.gentar.biology.mutation.mutation_ensembl.MutationEnsemblEnsemblEntry;
import org.gentar.biology.mutation.mutation_ensembl.mutationEnsemblMutationPartProjection;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.mutation.sequence.MutationSequenceService;
import org.gentar.biology.sequence.SequenceService;
import org.gentar.exceptions.NotFoundException;
import org.gentar.graphql.GraphQLConsumer;
import org.gentar.graphql.QueryBuilder;
import org.gentar.util.JsonHelper;
import org.springframework.stereotype.Component;

@Component
public class MutationServiceImpl implements MutationService {
    private final MutationRepository mutationRepository;
    private final SequenceService sequenceService;
    private final MutationSequenceService mutationSequenceService;
    private final MutationUpdater mutationUpdater;
    private final HistoryService<Mutation> historyService;
    private final MutationValidator mutationValidator;

    private final GraphQLConsumer graphQLConsumer;


    private static final String MUTATION_NOT_EXIST_ERROR = "Mutation %s does not exist.";

    public MutationServiceImpl(
            MutationRepository mutationRepository,
            SequenceService sequenceService,
            MutationSequenceService mutationSequenceService,
            MutationUpdater mutationUpdater,
            HistoryService<Mutation> historyService,
            MutationValidator mutationValidator, GraphQLConsumer graphQLConsumer) {
        this.mutationRepository = mutationRepository;
        this.sequenceService = sequenceService;
        this.mutationSequenceService = mutationSequenceService;
        this.mutationUpdater = mutationUpdater;
        this.historyService = historyService;
        this.mutationValidator = mutationValidator;
        this.graphQLConsumer = graphQLConsumer;
    }

    @Override
    public Mutation getById(Long id) {
        return mutationRepository.findFirstById(id);
    }

    @Override
    public Mutation getMutationByMinFailsIfNull(String min) {
        Mutation mutation = mutationRepository.findByMin(min);
        if (mutation == null) {
            throw new NotFoundException(String.format(MUTATION_NOT_EXIST_ERROR, min));
        }
        mutationValidator.validateReadPermissions(mutation);
        return mutation;
    }

    @Override
    public Mutation create(Mutation mutation) {
        validateData(mutation);
        Mutation createdMutation = mutationRepository.save(mutation);
        createdMutation.setMin(buildMin(createdMutation.getId()));
        saveMutationSequences(mutation);
        registerCreationInHistory(mutation);
        return createdMutation;
    }

    private void validateData(Mutation mutation) {
        mutationValidator.validateData(mutation);
    }

    private void registerCreationInHistory(Mutation mutation) {
        History history = historyService.buildCreationTrack(mutation, mutation.getId());
        historyService.saveTrackOfChanges(history);
    }

    @Override
    public History update(Mutation mutation) {
        Mutation originalMutation = new Mutation(getMutationByMinFailsIfNull(mutation.getMin()));
        return mutationUpdater.update(originalMutation, mutation);
    }

    @Override
    public List<History> getHistory(Mutation mutation) {
        return historyService.getHistoryByEntityNameAndEntityId(
            Mutation.class.getSimpleName(), mutation.getId());
    }

    @Override
    public MgiAlleleResponseDto updateMutationMgiAlleleId(List<MgiAlleleDto> mgiAlleleDtos) {
        MgiAlleleResponseDto mgiAlleleResponse = new MgiAlleleResponseDto();
        List<MgiAlleleDto> updatedMutations = new ArrayList<>();
        List<MgiAlleleDto> noUpdateNecessaryMutations = new ArrayList<>();
        List<MgiAlleleDto> nullMutations = new ArrayList<>();
        List<MgiAlleleDto> duplicateMutations = new ArrayList<>();


        for (MgiAlleleDto mgiAlleleDto : mgiAlleleDtos) {
            List<Mutation> mutations = mutationRepository.findBySymbol(mgiAlleleDto.getSymbol());
            if (mutations.isEmpty()) {
                nullMutations.add(mgiAlleleDto);
            } else if (mutations.size() > 1) {
                duplicateMutations.add(mgiAlleleDto);
            } else if (mgiAlleleDto.getAccId().equals(mutations.getFirst().getMgiAlleleId()) &&
                mgiAlleleDto.getSymbol().equals(mutations.getFirst().getSymbol())) {
                noUpdateNecessaryMutations.add(mgiAlleleDto);
            } else {
                mutations.getFirst().setMgiAlleleId(mgiAlleleDto.getAccId());
                update(mutations.getFirst());
                updatedMutations.add(mgiAlleleDto);
            }
        }
        mgiAlleleResponse.setDuplicateMutation(duplicateMutations);
        mgiAlleleResponse.setNoMutation(nullMutations);
        mgiAlleleResponse.setNoUpdateNecessary(noUpdateNecessaryMutations);
        mgiAlleleResponse.setUpdated(updatedMutations);
        return mgiAlleleResponse;
    }

    @Override
    public List<CombinedMutationEnsemblDto> getCombinedMutationEnsembl(List<String> mins) throws IOException {

        List<mutationEnsemblMutationPartProjection> mutationEnsemblMutationPartProjections = mutationRepository.findMutationEnsembleMutationPartByMins(mins);
        List<String> mgis = mutationEnsemblMutationPartProjections.stream().map(mutationEnsemblMutationPartProjection::getAccId).toList();

        String result = mutationEnsemblEnsemblEntryJsonResult(mgis);

        var mutationEnsemblEnsemblEntry =
                JsonHelper.fromJson(result, MutationEnsemblEnsemblEntry.class);

        List<MutationEnsemblEnsemblPartDto> mutationEnsemblEnsemblPartDtos = mutationEnsemblEnsemblEntry.getMutationEnsemblEnsemblPartDtos();

        List<MutationEnsemblEnsemblPartDto> processedMutationEnsemblEnsemblPartDtos = processMutationEnsemblEnsemblPart(mutationEnsemblEnsemblPartDtos);

        List<CombinedMutationEnsemblDto> combinedMutationEnsemblDtos = new ArrayList<>();

        // Create a map where each symbol points to a list of GeneInfoDTOs
        Map<String, List<mutationEnsemblMutationPartProjection>> mutationEnsemblMutationPartMap = new HashMap<>();
        for (mutationEnsemblMutationPartProjection mutationEnsemblMutationPartProjection : mutationEnsemblMutationPartProjections) {
            mutationEnsemblMutationPartMap.computeIfAbsent(mutationEnsemblMutationPartProjection.getSymbol(), k -> new ArrayList<>()).add(mutationEnsemblMutationPartProjection);
        }

        for (MutationEnsemblEnsemblPartDto mouseGene : processedMutationEnsemblEnsemblPartDtos) {
            String symbol = mouseGene.getSymbol();

            if (mutationEnsemblMutationPartMap.containsKey(symbol)) {
                List<mutationEnsemblMutationPartProjection> geneInfos = mutationEnsemblMutationPartMap.get(symbol);

                for (mutationEnsemblMutationPartProjection geneInfo : geneInfos) {
                    CombinedMutationEnsemblDto combinedData = getCombinedMutationEnsemblDto(mouseGene, geneInfo);
                    combinedMutationEnsemblDtos.add(combinedData);
                }
            }
        }

        return combinedMutationEnsemblDtos;
    }

    private String mutationEnsemblEnsemblEntryJsonResult(List<String> mgis) {
        List<String> conditions = new ArrayList<>();
        conditions.add(QueryBuilder.getColumnInExactMatchCondition("mgi_gene_acc_id", mgis));

        String query = QueryBuilder.getInstance()
                .withRoot("mouse_gene")
                .withOrCondition(conditions)
                .withFields(List.of("ensembl_gene_acc_id", "mgi_gene_acc_id", "ensembl_chromosome", "ensembl_start", "ensembl_stop", "symbol", "ensembl_strand"))
                .build();

        return graphQLConsumer.executeQuery(query);
    }

    private static CombinedMutationEnsemblDto getCombinedMutationEnsemblDto(MutationEnsemblEnsemblPartDto mouseGene, mutationEnsemblMutationPartProjection geneInfo) {
        CombinedMutationEnsemblDto combinedData = new CombinedMutationEnsemblDto();
        combinedData.setMin(geneInfo.getMin());
        combinedData.setEnsemblGeneAccId(mouseGene.getEnsemblGeneAccId());
        combinedData.setMgiGeneAccId(mouseGene.getMgiGeneAccId());
        combinedData.setEnsemblChromosome(mouseGene.getEnsemblChromosome());
        combinedData.setEnsemblStart(mouseGene.getEnsemblStart());
        combinedData.setEnsemblStop(mouseGene.getEnsemblStop());
        combinedData.setSymbol(mouseGene.getSymbol());
        combinedData.setEnsemblStrand(mouseGene.getEnsemblStrand());
        return combinedData;
    }


    public List<MutationEnsemblEnsemblPartDto> processMutationEnsemblEnsemblPart(List<MutationEnsemblEnsemblPartDto> mutationEnsemblEnsemblPartDtos) {
        for (MutationEnsemblEnsemblPartDto mutationEnsemblEnsemblPartDto : mutationEnsemblEnsemblPartDtos) {
            if ("x".equalsIgnoreCase(mutationEnsemblEnsemblPartDto.getEnsemblChromosome())) {
                mutationEnsemblEnsemblPartDto.setEnsemblChromosome("NOT SPECIFIED");
            } else {
                mutationEnsemblEnsemblPartDto.setEnsemblChromosome("chr" + mutationEnsemblEnsemblPartDto.getEnsemblChromosome());
            }

            if (mutationEnsemblEnsemblPartDto.getEnsemblStrand() == null || mutationEnsemblEnsemblPartDto.getEnsemblStrand().isEmpty()) {
                mutationEnsemblEnsemblPartDto.setEnsemblStrand(".");
            }
        }
        return mutationEnsemblEnsemblPartDtos;

    }


    private String buildMin(Long id) {
        String identifier = String.format("%0" + 12 + "d", id);
        identifier = "MIN:" + identifier;
        return identifier;
    }

    private void saveMutationSequences(Mutation mutation) {
        Set<MutationSequence> mutationSequences = mutation.getMutationSequences();
        if (mutationSequences != null) {
            mutationSequences.forEach(x -> {
                sequenceService.createSequence(x.getSequence());
                mutationSequenceService.createSequence(x);
            });
        }
    }
}
