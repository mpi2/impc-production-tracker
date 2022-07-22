package org.gentar.biology.outcome;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.common.history.HistoryDTO;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.helpers.ChangeResponseCreator;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class OutcomeController
{
    private final OutcomeService outcomeService;
    private final PlanService planService;
    private final OutcomeRequestProcessor outcomeRequestProcessor;
    private final OutcomeResponseMapper outcomeResponseMapper;
    private final OutcomeCreationMapper outcomeCreationMapper;
    private final ChangeResponseCreator changeResponseCreator;
    private final HistoryMapper historyMapper;

    public OutcomeController(
        OutcomeService outcomeService,
        PlanService planService,
        OutcomeRequestProcessor outcomeRequestProcessor,
        OutcomeResponseMapper outcomeResponseMapper,
        OutcomeCreationMapper outcomeCreationMapper,
        ChangeResponseCreator changeResponseCreator,
        HistoryMapper historyMapper)
    {
        this.outcomeService = outcomeService;
        this.planService = planService;
        this.outcomeRequestProcessor = outcomeRequestProcessor;
        this.outcomeResponseMapper = outcomeResponseMapper;
        this.outcomeCreationMapper = outcomeCreationMapper;
        this.changeResponseCreator = changeResponseCreator;
        this.historyMapper = historyMapper;
    }

    @GetMapping(value = {"outcomes/{tpo}"})
    public OutcomeResponseDTO findOneByTpo(@PathVariable String tpo)
    {
        Outcome outcome = outcomeService.getByTpoFailsIfNotFound(tpo);
        return outcomeResponseMapper.toDto(outcome);
    }

    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}"})
    public OutcomeResponseDTO findOneByPlanAndTpo(
        @PathVariable String pin, @PathVariable String tpo)
    {
        Outcome outcome = outcomeService.getOutcomeByPinAndTpo(pin, tpo);
        return outcomeResponseMapper.toDto(outcome);
    }

    @GetMapping(value = {"plans/{pin}/outcomes"})
    public ResponseEntity<CollectionModel<OutcomeResponseDTO>> findAllByPlan(@PathVariable String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        Set<Outcome> outcomes = plan.getOutcomes();
        return ResponseEntity.ok(CollectionModel.of(outcomeResponseMapper.toDtos(outcomes)));
    }

    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}/history"})
    public List<HistoryDTO> getPlanHistory(
        @PathVariable String pin, @PathVariable String tpo)
    {
        Outcome outcome = outcomeService.getOutcomeByPinAndTpo(pin, tpo);
        List<HistoryDTO> historyDTOS =
            historyMapper.toDtos(outcomeService.getOutcomeHistory(outcome));
        return historyDTOS;
    }

    /**
     * Create an outcome in the system.
     * @param pin Public identifier for the plan.
     * @param outcomeCreationDTO DTO with request to creation of an outcome.
     * @return The DTO with the outcome of the created outcome.
     */
    @PostMapping(value = {"plans/{pin}/outcomes"})
    public ChangeResponse create(
        @PathVariable String pin, @RequestBody OutcomeCreationDTO outcomeCreationDTO)
    {
        Outcome outcome = outcomeCreationMapper.toEntity(outcomeCreationDTO);
       boolean canCreate = planService.canCreateOutcome(pin);
       if(!canCreate) {
           throw new UserOperationFailedException("You can not create an outcome in current state");
       }
        outcomeService.associateOutcomeToPlan(outcome, pin);
        Outcome createdOutcome = outcomeService.create(outcome);
        return buildChangeResponse(
            pin, createdOutcome.getTpo(), outcomeService.getOutcomeHistory(createdOutcome));
    }

    @PutMapping(value = {"plans/{pin}/outcomes/{tpo}"})
    public ChangeResponse update(
        @PathVariable String pin,
        @PathVariable String tpo,
        @RequestBody OutcomeUpdateDTO outcomeUpdateDTO)
    {
        outcomeRequestProcessor.validateAssociation(pin, tpo);
        Outcome outcome = getOutcomeToUpdate(tpo, outcomeUpdateDTO);
        History history = outcomeService.update(outcome);
        return buildChangeResponse(pin, tpo, history);
    }

    /**
     * Creates the relationship between an outcome and one or more mutations identified by the
     * mutationIds values.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @param mins List of public mutation ids.
     * @return ChangeResponse record with the changes.
     */
    @PostMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations/associate"})
    public ChangeResponse createMutationAssociations(
        @PathVariable String pin,
        @PathVariable String tpo,
        @RequestParam(value = "min", required = false) List<String> mins)
    {
        History history = outcomeService.createMutationsAssociations(pin, tpo, mins);
        return buildChangeResponse(pin, tpo, history);
    }

    /**
     * Deletes the relationship between an outcome and one or more mutations identified by the
     * mutationIds values.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @param mins List of public mutation ids.
     * @return ChangeResponse record with the changes.
     */
    @DeleteMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations/deleteAssociations"})
    public ChangeResponse deleteMutationAssociations(
        @PathVariable String pin,
        @PathVariable String tpo,
        @RequestParam(value = "min", required = false) List<String> mins)
    {
        History history = outcomeService.deleteMutationsAssociations(pin, tpo, mins);
        return buildChangeResponse(pin, tpo, history);
    }

    /**
     * Get an Outcome object based on OutcomeDTO using the fields that can be updated by the user.
     * @param outcomeUpdateDTO outcome sent by the user.
     * @return The original outcome with the allowed modifications specified in the dto.
     */
    private Outcome getOutcomeToUpdate(String tpo, OutcomeUpdateDTO outcomeUpdateDTO)
    {
        Outcome currentOutcome = outcomeService.getByTpoFailsIfNotFound(tpo);
        return outcomeRequestProcessor.getOutcomeToUpdate(currentOutcome, outcomeUpdateDTO);
    }

    private ChangeResponse buildChangeResponse(String pin, String tpo, History history)
    {
        Link link = buildOutcomeLink(pin, tpo);
        return changeResponseCreator.create(link, history);
    }

    private ChangeResponse buildChangeResponse(String pin, String tpo, Collection<History> histories)
    {
        Link link = buildOutcomeLink(pin, tpo);
        return changeResponseCreator.create(link, histories);
    }

    private Link buildOutcomeLink(String pin, String tpo)
    {
        return linkTo(methodOn(OutcomeController.class).findOneByPlanAndTpo(pin, tpo)).withSelfRel();
    }
}
