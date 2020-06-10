package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanController;
import org.gentar.biology.plan.PlanService;
import org.gentar.helpers.ChangeResponseCreator;
import org.gentar.helpers.LinkUtil;
import org.gentar.helpers.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class OutcomeController
{
    private OutcomeService outcomeService;
    private PlanService planService;
    private OutcomeRequestProcessor outcomeRequestProcessor;
    private OutcomeResponseMapper outcomeResponseMapper;
    private OutcomeCreationMapper outcomeCreationMapper;
    private ChangeResponseCreator changeResponseCreator;

    public OutcomeController(
        OutcomeService outcomeService,
        PlanService planService,
        OutcomeRequestProcessor outcomeRequestProcessor,
        OutcomeResponseMapper outcomeResponseMapper,
        OutcomeCreationMapper outcomeCreationMapper,
        ChangeResponseCreator changeResponseCreator)
    {
        this.outcomeService = outcomeService;
        this.planService = planService;
        this.outcomeRequestProcessor = outcomeRequestProcessor;
        this.outcomeResponseMapper = outcomeResponseMapper;
        this.outcomeCreationMapper = outcomeCreationMapper;
        this.changeResponseCreator = changeResponseCreator;
    }

    @GetMapping(value = {"/outcomes"})
    public ResponseEntity<OutcomeResponseDTO> findAll(
        Pageable pageable, PagedResourcesAssembler<OutcomeResponseDTO> assembler)
    {
        List<Outcome> outcomes = outcomeService.getOutcomes();

        Page<Outcome> paginatedContent = PaginationHelper.createPage(outcomes, pageable);
        Page<OutcomeResponseDTO> outcomeDTOPage = paginatedContent.map(x -> outcomeResponseMapper.toDto(x));

        PagedModel<EntityModel<OutcomeResponseDTO>> pr =
            assembler.toModel(
                outcomeDTOPage,
                linkTo(OutcomeController.class).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity(pr, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}"})
    public OutcomeResponseDTO findOneByPlanAndTpo(
        @PathVariable String pin, @PathVariable String tpo)
    {
        Outcome outcome = outcomeService.getOutcomeByPinAndTpo(pin, tpo);
        return outcomeResponseMapper.toDto(outcome);
    }

    @GetMapping(value = {"plans/{pin}/outcomes"})
    public List<OutcomeResponseDTO> findAllByPlan(@PathVariable String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        Set<Outcome> outcomes = plan.getOutcomes();
        return outcomeResponseMapper.toDtos(outcomes);
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
        outcomeService.associateOutcomeToPlan(outcome, pin);
        Outcome createdOutcome = outcomeService.create(outcome);
        return buildChangeResponse(
            pin, createdOutcome.getTpo(), outcomeService.getOutcomeHistory(createdOutcome));
    }

    @PutMapping(value = {"plans/{pin}/outcomes/{tpo}"})
    public ChangeResponse update(
        @PathVariable String pin, @PathVariable String tpo, @RequestBody OutcomeDTO outcomeDTO)
    {
        outcomeRequestProcessor.validateAssociation(pin, tpo);
        Outcome outcome = getOutcomeToUpdate(outcomeDTO);
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
    @PostMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations"})
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
    @DeleteMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations"})
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
     * @param outcomeDTO outcome sent by the user.
     * @return The original outcome with the allowed modifications specified in the dto.
     */
    private Outcome getOutcomeToUpdate(OutcomeDTO outcomeDTO)
    {
        Outcome currentOutcome = outcomeService.getByTpoFailsIfNotFound(outcomeDTO.getTpo());
        return outcomeRequestProcessor.getOutcomeToUpdate(currentOutcome, outcomeDTO);
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
