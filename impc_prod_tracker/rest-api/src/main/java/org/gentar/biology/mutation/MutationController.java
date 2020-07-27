package org.gentar.biology.mutation;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.OutcomeResponseDTO;
import org.gentar.biology.outcome.OutcomeService;
import org.gentar.biology.plan.Plan;
import org.gentar.common.history.HistoryDTO;
import org.gentar.helpers.ChangeResponseCreator;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * Gets all the mutations for a specific outcome.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @return Collection of mutations.
     */
    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations"})
    public ResponseEntity<CollectionModel<MutationResponseDTO>> getAllMutationsByOutcome(
        @PathVariable String pin, @PathVariable String tpo)
    {
        List<MutationResponseDTO> mutationResponseDTOS = new ArrayList<>();
        Outcome outcome = outcomeService.getOutcomeByPinAndTpo(pin, tpo);
        if (outcome != null)
        {
            mutationResponseDTOS = mutationResponseMapper.toDtos(outcome.getMutations());
        }
        return ResponseEntity.ok(CollectionModel.of(mutationResponseDTOS));
    }

    /**
     * Gets a mutation in an outcome.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @param min Public identifier of the mutation.
     * @return Mutation DTO
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
        return buildChangeResponse(pin, tpo, min,  history);
    }

    private ChangeResponse buildChangeResponse(String pin, String tpo, String min,  History history)
    {
        Link link = buildMutationLink(pin, tpo, min);
        return changeResponseCreator.create(link, history);
    }

    private Link buildMutationLink(String pin, String tpo, String min)
    {
        return linkTo(
            methodOn(MutationController.class)
                .findMutationInOutcomeById(pin, tpo, min)).withSelfRel();
    }

    private Mutation getMutationToUpdate(
        String pin, String tpo, String min, MutationUpdateDTO mutationUpdateDTO)
    {
        Mutation currentMutation = outcomeService.getMutationByPinTpoAndMin(pin, tpo, min);
        return mutationRequestProcessor.getMutationToUpdate(currentMutation, mutationUpdateDTO);
    }
}
