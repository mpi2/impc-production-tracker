package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.mutation.mutation_ensembl.CombinedMutationEnsemblDto;
import org.gentar.biology.mutation.symbolConstructor.AlleleSymbolConstructor;
import org.gentar.biology.mutation.symbolConstructor.SymbolSuggestionRequest;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeService;
import org.gentar.common.history.HistoryDTO;
import org.gentar.helpers.ChangeResponseCreator;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class MutationController
{
    private final OutcomeService outcomeService;
    private final MutationResponseMapper mutationResponseMapper;
    private final MutationRequestProcessor mutationRequestProcessor;
    private final MutationService mutationService;
    private final ChangeResponseCreator changeResponseCreator;
    private final HistoryMapper historyMapper;

    public MutationController(
        OutcomeService outcomeService,
        MutationResponseMapper mutationResponseMapper,
        MutationRequestProcessor mutationRequestProcessor,
        MutationService mutationService,
        ChangeResponseCreator changeResponseCreator,
        HistoryMapper historyMapper)
    {
        this.outcomeService = outcomeService;
        this.mutationResponseMapper = mutationResponseMapper;
        this.mutationRequestProcessor = mutationRequestProcessor;
        this.mutationService = mutationService;
        this.changeResponseCreator = changeResponseCreator;
        this.historyMapper = historyMapper;
    }

    @GetMapping(value = {"mutations/{min}"})
    public MutationResponseDTO findMutationByMin(@PathVariable String min)
    {
        Mutation mutation = mutationService.getMutationByMinFailsIfNull(min);
        return mutationResponseMapper.toDto(mutation);
    }

    /**
     * Gets all the mutations for a specific outcome.
     * @param tpo Public identifier of the outcome.
     * @return Collection of mutations.
     */
    @GetMapping(value = {"outcomes/{tpo}/mutations"})
    public ResponseEntity<CollectionModel<MutationResponseDTO>>
    getAllMutationsByOutcome(@PathVariable String tpo)
    {
        List<MutationResponseDTO> mutationResponseDTOS;
        Outcome outcome = outcomeService.getByTpoFailsIfNotFound(tpo);
        mutationResponseDTOS = getMutationsDtosByOutcome(outcome);
        return ResponseEntity.ok(CollectionModel.of(mutationResponseDTOS));
    }

    /**
     * Gets a mutation in an outcome.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @param min Public identifier of the mutation.
     * @return Mutation DTO
     */
    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations/{min}"})
    public MutationResponseDTO findMutationInOutcomeById(
        @PathVariable String pin, @PathVariable String tpo, @PathVariable String min)
    {
        Mutation mutation = outcomeService.getMutationByPinTpoAndMin(pin, tpo, min);
        return mutationResponseMapper.toDto(mutation);
    }

    /**
     * Gets a mutation in an outcome.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @param min Public identifier of the mutation.
     * @return Mutation DTO
     */
    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations/{min}/history"})
    public List<HistoryDTO> findMutationHistoryInOutcomeById(
        @PathVariable String pin, @PathVariable String tpo, @PathVariable String min)
    {
        Mutation mutation = outcomeService.getMutationByPinTpoAndMin(pin, tpo, min);
        return historyMapper.toDtos(mutationService.getHistory(mutation));
    }

    /**
     * Gets all the mutations for a specific outcome accessing it through the plan.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @return Collection of mutations.
     */
    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations"})
    public ResponseEntity<CollectionModel<MutationResponseDTO>> getAllMutationsByPlanAndOutcome(
        @PathVariable String pin, @PathVariable String tpo)
    {
        List<MutationResponseDTO> mutationResponseDTOS;
        Outcome outcome = outcomeService.getOutcomeByPinAndTpo(pin, tpo);
        mutationResponseDTOS = getMutationsDtosByOutcome(outcome);
        return ResponseEntity.ok(CollectionModel.of(mutationResponseDTOS));
    }

    private List<MutationResponseDTO> getMutationsDtosByOutcome(Outcome outcome)
    {
        List<MutationResponseDTO> mutationResponseDTOS = new ArrayList<>();
        if (outcome != null)
        {
            mutationResponseDTOS = mutationResponseMapper.toDtos(outcome.getMutations());
        }
        return mutationResponseDTOS;
    }

    /**
     * Updates a mutation in an outcome.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @param min Public identifier of the mutation.
     * @return ChangeResponse object with information about the change.
     */
    @PutMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations/{min}"})
    public ChangeResponse updateMutationInOutcomeById(
        @PathVariable String pin,
        @PathVariable String tpo,
        @PathVariable String min,
        @RequestBody MutationUpdateDTO mutationUpdateDTO)
    {
        Mutation mutation = getMutationToUpdate(pin, tpo, min, mutationUpdateDTO);
        History history = mutationService.update(mutation);
        return buildChangeResponse(pin, tpo, min, history);
    }

    @PostMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations"})
    public ChangeResponse createMutationInOutcome(
        @PathVariable String pin,
        @PathVariable String tpo,
        @RequestBody MutationCreationDTO mutationCreationDTO)
    {
        Outcome outcome = outcomeService.getOutcomeByPinAndTpo(pin, tpo);
        Mutation mutation = getMutationToCreate(outcome, mutationCreationDTO);
        Mutation createdMutation = mutationService.create(mutation);
        return buildChangeResponse(pin, tpo, createdMutation);
    }

    private ChangeResponse buildChangeResponse(String pin, String tpo, Mutation mutation)
    {
        Link link = buildMutationLink(pin, tpo, mutation.getMin());
        return changeResponseCreator.create(link, mutationService.getHistory(mutation));
    }

    private ChangeResponse buildChangeResponse(String pin, String tpo, String min, History history)
    {
        Link link = buildMutationLink(pin, tpo, min);

        return changeResponseCreator.create(link, history);
    }

    private Link buildMutationLink(String pin, String tpo, String min)
    {
        Link link = linkTo(
            methodOn(MutationController.class)
                .findMutationInOutcomeById(pin, tpo, min)).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        return link;
    }

    private Mutation getMutationToCreate(Outcome outcome, MutationCreationDTO mutationCreationDTO)
    {
        return mutationRequestProcessor.getMutationToCreate(outcome, mutationCreationDTO);
    }

    private Mutation getMutationToUpdate(
        String pin, String tpo, String min, MutationUpdateDTO mutationUpdateDTO)
    {
        Mutation currentMutation = outcomeService.getMutationByPinTpoAndMin(pin, tpo, min);
        return mutationRequestProcessor.getMutationToUpdate(currentMutation, mutationUpdateDTO);
    }

    @PostMapping(value = {"plans/{pin}/outcomes/suggestedSymbol"})
    public String getSuggestedSymbol(
        @PathVariable String pin, @RequestBody MutationUpdateDTO mutationUpdateDTO)
    {
        String result = "";
        SymbolSuggestionRequestDTO symbolSuggestionRequestDTO =
            mutationUpdateDTO.getSymbolSuggestionRequestDTO();
        if (symbolSuggestionRequestDTO != null)
        {
            SymbolSuggestionRequest symbolSuggestionRequest =
                mutationRequestProcessor.buildSymbolSuggestionRequest(symbolSuggestionRequestDTO, pin);
            AlleleSymbolConstructor alleleSymbolConstructor =
                mutationRequestProcessor.getAlleleSymbolConstructor(pin);
            if (alleleSymbolConstructor != null)
            {
                Mutation mutation = mutationRequestProcessor.getSimpleMappedMutation(mutationUpdateDTO);
                result = alleleSymbolConstructor.calculateAlleleSymbol(symbolSuggestionRequest, mutation);
            }
        }
        return result;
    }

    @PostMapping(value = {"/mutation/get_mutation_with_ensembl"})
    public List<CombinedMutationEnsemblDto> getSuggestedSymbol(@RequestBody List<String> mins) throws IOException {
        return mutationService.getCombinedMutationEnsembl(mins);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
