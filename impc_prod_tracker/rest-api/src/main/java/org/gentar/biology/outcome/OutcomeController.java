package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.common.history.HistoryDTO;
import org.gentar.helpers.LinkUtil;
import org.gentar.helpers.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class OutcomeController
{
    private OutcomeServiceImpl outcomeService;
    private OutcomeMapper outcomeMapper;
    private PlanService planService;
    private OutcomeRequestProcessor outcomeRequestProcessor;
    private HistoryMapper historyMapper;

    public OutcomeController(
        OutcomeServiceImpl outcomeService,
        OutcomeMapper outcomeMapper,
        PlanService planService,
        OutcomeRequestProcessor outcomeRequestProcessor,
        HistoryMapper historyMapper)
    {
        this.outcomeService = outcomeService;
        this.outcomeMapper = outcomeMapper;
        this.planService = planService;
        this.outcomeRequestProcessor = outcomeRequestProcessor;
        this.historyMapper = historyMapper;
    }

    @GetMapping(value = {"/outcomes"})
    public ResponseEntity<OutcomeDTO> findAll(
        Pageable pageable, PagedResourcesAssembler<OutcomeDTO> assembler)
    {
        List<Outcome> outcomes = outcomeService.getOutcomes();

        Page<Outcome> paginatedContent = PaginationHelper.createPage(outcomes, pageable);
        Page<OutcomeDTO> outcomeDTOPage = paginatedContent.map(x -> outcomeMapper.toDto(x));

        PagedModel<EntityModel<OutcomeDTO>> pr =
            assembler.toModel(
                outcomeDTOPage,
                linkTo(OutcomeController.class).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity(pr, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = {"plans/{pin}/outcomes"})
    public List<OutcomeDTO> findAllByPlan(@PathVariable String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        Set<Outcome> outcomes = plan.getOutcomes();
        List<OutcomeDTO> outcomeDTOS = outcomeMapper.toDtos(outcomes);
        return outcomeDTOS;
    }

    /**
     * Create an outcome in the system.
     * @param outcomeDTO DTO with the representation of the outcome.
     * @return The DTO with the outcome of the created outcome.
     */
    @PostMapping(value = {"plans/{pin}/outcomes"})
    public OutcomeDTO create(@RequestBody OutcomeDTO outcomeDTO)
    {
        Outcome outcome = outcomeMapper.toEntity(outcomeDTO);
        Outcome createdOutcome = outcomeService.create(outcome);
        return outcomeMapper.toDto(createdOutcome);
    }

    @PutMapping(value = {"plans/{pin}/outcomes/{tpo}"})
    public HistoryDTO update(@PathVariable String pin, @PathVariable String tpo, @RequestBody OutcomeDTO outcomeDTO)
    {
        HistoryDTO historyDTO = new HistoryDTO();
        outcomeRequestProcessor.validateAssociation(pin, tpo);
        Outcome outcome = getOutcomeToUpdate(tpo, outcomeDTO);
        History history = outcomeService.update(outcome);

        if (history != null)
        {
            historyDTO = historyMapper.toDto(history);
        }
        return historyDTO;
    }

    /**
     * Get an Outcome object based on OutcomeDTO using the fields that can be updated by the user.
     * @param tpo public id of the outcome.
     * @param outcomeDTO outcome sent by the user.
     * @return The original outcome with the allowed modifications specified in the dto.
     */
    private Outcome getOutcomeToUpdate(String tpo, OutcomeDTO outcomeDTO)
    {
        Outcome currentOutcome = outcomeService.getByTpoFailsIfNotFound(tpo);
        Outcome newOutcome = new Outcome(currentOutcome);
        return outcomeRequestProcessor.getOutcomeToUpdate(newOutcome, outcomeDTO);
    }

}
