package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.plan.*;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PhenotypingStageController {
    private HistoryMapper historyMapper;
    private PhenotypingStageService phenotypingStageService;
    private PhenotypingStageMapper phenotypingStageMapper;

    private PhenotypingStageCreationMapper phenotypingStageCreationMapper;
    private PhenotypingStageResponseMapper phenotypingStageResponseMapper;
    private PhenotypingStageUpdateMapper phenotypingStageUpdateMapper;
    private PhenotypingStageCommonMapper phenotypingStageCommonMapper;

    private PlanService planService;
    private UpdatePhenotypingStageRequestProcessor updatePhenotypingStageRequestProcessor;

    public PhenotypingStageController(HistoryMapper historyMapper, PhenotypingStageService phenotypingStageService,
                                      PhenotypingStageMapper phenotypingStageMapper,
                                      PhenotypingStageCreationMapper phenotypingStageCreationMapper,
                                      PhenotypingStageResponseMapper phenotypingStageResponseMapper,
                                      PhenotypingStageUpdateMapper phenotypingStageUpdateMapper,
                                      PhenotypingStageCommonMapper phenotypingStageCommonMapper,
                                      PlanService planService,
                                      UpdatePhenotypingStageRequestProcessor updatePhenotypingStageRequestProcessor)
    {
        this.historyMapper = historyMapper;
        this.phenotypingStageService = phenotypingStageService;
        this.phenotypingStageMapper = phenotypingStageMapper;
        this.phenotypingStageCreationMapper = phenotypingStageCreationMapper;
        this.phenotypingStageResponseMapper = phenotypingStageResponseMapper;
        this.phenotypingStageUpdateMapper = phenotypingStageUpdateMapper;
        this.phenotypingStageCommonMapper = phenotypingStageCommonMapper;
        this.planService = planService;
        this.updatePhenotypingStageRequestProcessor = updatePhenotypingStageRequestProcessor;
    }

    /**
     * Get all the phenotyping stages in the system.
     * @return A collection of {@link PhenotypingStageDTO} objects.
     */
    @GetMapping(value = {"/phenotypingStages"})
    public ResponseEntity<PhenotypingStageDTO> findAll(
            Pageable pageable, PagedResourcesAssembler<PhenotypingStageDTO> assembler)
    {
        List<PhenotypingStage> phenotypingStages = phenotypingStageService.getPhenotypingStages();

        Page<PhenotypingStage> paginatedContent = PaginationHelper.createPage(phenotypingStages, pageable);
        Page<PhenotypingStageDTO> phenotypingStageDTOPage = paginatedContent.map(x -> phenotypingStageMapper.toDto(x));

        PagedModel<EntityModel<PhenotypingStageDTO>> pr =
                assembler.toModel(
                        phenotypingStageDTOPage,
                        linkTo(PhenotypingStageController.class).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity(pr, responseHeaders, HttpStatus.OK);
    }

    /**
     * Get all the phenotyping stages for a plan.
     * @param pin PIN id for the plan.
     * @return The DTOs with the phenotyping stages for the plan.
     */
    @GetMapping(value = {"plans/{pin}/phenotypingStages"})
    public List<PhenotypingStageDTO> findAllByPlan(@PathVariable String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        Set<PhenotypingStage> phenotypingStages = plan.getPhenotypingAttempt().getPhenotypingStages();
        return phenotypingStageMapper.toDtos(phenotypingStages);
    }

    /**
     * Create a phenotyping stage in the system.
     * @param phenotypingStageCreationDTO DTO with the representation of the phenotyping stage.
     * @return The DTO with the phenotyping stage of the created phenotyping stage.
     */
    @PostMapping(value = {"plans/{pin}/phenotypingStages"})
    public PhenotypingStageCreationDTO create(@PathVariable String pin, @RequestBody PhenotypingStageCreationDTO phenotypingStageCreationDTO)
    {
        PhenotypingStage phenotypingStage = phenotypingStageCreationMapper.toEntity(phenotypingStageCreationDTO);
        phenotypingStage.setPhenotypingAttempt(findAttempt(pin));
        PhenotypingStage createdPhenotypingStage = phenotypingStageService.create(phenotypingStage);
        return phenotypingStageCreationMapper.toDto(createdPhenotypingStage);
    }

    private PhenotypingAttempt findAttempt(String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        return plan.getPhenotypingAttempt();
    }

    @PutMapping(value = {"plans/{pin}/phenotypingStages/{id}"})
    public HistoryDTO update(
            @PathVariable String pin, @PathVariable String id, @RequestBody PhenotypingStageDTO phenotypingStageDTO)
    {
        HistoryDTO historyDTO = new HistoryDTO();
        updatePhenotypingStageRequestProcessor.validateAssociation(pin, id);
        PhenotypingStage phenotypingStage = getPhenotypingStageToUpdate(phenotypingStageDTO);
        History history = phenotypingStageService.update(phenotypingStage);

        if (history != null)
        {
            historyDTO = historyMapper.toDto(history);
        }
        return historyDTO;
    }

    /**
     * Get an PhenotypingStage object based on PhenotypingStageDTO using the fields that can be updated by the user.
     * @param phenotypingStageDTO Phenotyping Stage sent by the user.
     * @return The original phenotyping stage with the allowed modifications specified in the dto.
     */
    private PhenotypingStage getPhenotypingStageToUpdate(PhenotypingStageDTO phenotypingStageDTO)
    {
        PhenotypingStage currentphenotypingStage = phenotypingStageService.getPhenotypingStageById(phenotypingStageDTO.getId());
        return updatePhenotypingStageRequestProcessor.getPhenotypeStageToUpdate(currentphenotypingStage, phenotypingStageDTO);
    }
}
