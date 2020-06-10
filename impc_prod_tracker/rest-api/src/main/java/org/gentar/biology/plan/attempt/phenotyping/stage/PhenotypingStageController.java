package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
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
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PhenotypingStageController
{
    private PhenotypingStageService phenotypingStageService;
    private PlanService planService;
    private PhenotypingStageRequestProcessor phenotypingStageRequestProcessor;
    private HistoryMapper historyMapper;
    private PhenotypingStageResponseMapper phenotypingStageResponseMapper;
    private PhenotypingStageCreationMapper phenotypingStageCreationMapper;

    public PhenotypingStageController (
            PhenotypingStageService phenotypingStageService,
            PlanService planService,
            PhenotypingStageRequestProcessor phenotypingStageRequestProcessor,
            HistoryMapper historyMapper,
            PhenotypingStageResponseMapper phenotypingStageResponseMapper,
            PhenotypingStageCreationMapper phenotypingStageCreationMapper)
    {
        this.phenotypingStageService = phenotypingStageService;
        this.planService = planService;
        this.phenotypingStageRequestProcessor = phenotypingStageRequestProcessor;
        this.historyMapper = historyMapper;
        this.phenotypingStageResponseMapper = phenotypingStageResponseMapper;
        this.phenotypingStageCreationMapper = phenotypingStageCreationMapper;
    }

    @GetMapping(value = {"/phenotypingStages"})
    public ResponseEntity<PhenotypingStageResponseDTO> findAll(
            Pageable pageable, PagedResourcesAssembler<PhenotypingStageResponseDTO> assembler)
    {
        List<PhenotypingStage> phenotypingStages = phenotypingStageService.getPhenotypingStages();

        Page<PhenotypingStage> paginatedContent = PaginationHelper.createPage(phenotypingStages, pageable);
        Page<PhenotypingStageResponseDTO> phenotypingStageDTOPage = paginatedContent.map(x -> phenotypingStageResponseMapper.toDto(x));

        PagedModel<EntityModel<PhenotypingStageResponseDTO>> pr =
                assembler.toModel(
                        phenotypingStageDTOPage,
                        linkTo(PhenotypingStageController.class).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity(pr, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = {"plans/{pin}/phenotypingStages/{psn}"})
    public PhenotypingStageResponseDTO findOneByPlanAndPsn(
            @PathVariable String pin, @PathVariable String psn)
    {
        PhenotypingStage phenotypingStage = phenotypingStageService.getPhenotypingStageByPinAndPsn(pin, psn);
        return phenotypingStageResponseMapper.toDto(phenotypingStage);
    }

    @GetMapping(value = {"plans/{pin}/phenotypingStages"})
    public List<PhenotypingStageResponseDTO> findAllByPlan(@PathVariable String pin)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        Set<PhenotypingStage> phenotypingStages = plan.getPhenotypingAttempt().getPhenotypingStages();
        return phenotypingStageResponseMapper.toDtos(phenotypingStages);
    }

    /**
     * Create a phenotyping stage in the system.
     * @param phenotypingStageCreationDTO DTO with the representation of the phenotyping stage.
     * @return The DTO with the phenotyping stage of the created phenotyping stage.
     */
    @PostMapping(value = {"plans/{pin}/phenotypingStages"})
    public ChangeResponse create(@PathVariable String pin,
                                 @RequestBody PhenotypingStageCreationDTO phenotypingStageCreationDTO)
    {
        PhenotypingStage phenotypingStageToBeCreated =
            phenotypingStageCreationMapper.toEntity(phenotypingStageCreationDTO);
        Plan plan = planService.getNotNullPlanByPin(pin);
        phenotypingStageToBeCreated.setPhenotypingAttempt(plan.getPhenotypingAttempt());
        PhenotypingStage createdPhenotypingStage =
            phenotypingStageService.create(phenotypingStageToBeCreated);
        return buildChangeResponse(createdPhenotypingStage);
    }

    private ChangeResponse buildChangeResponse(PhenotypingStage phenotypingStage)
    {
        List<HistoryDTO> historyList =
            historyMapper.toDtos(phenotypingStageService.getPhenotypingStageHistory(phenotypingStage));
        return buildChangeResponse(phenotypingStage, historyList);
    }
    private ChangeResponse buildChangeResponse(
        PhenotypingStage phenotypingStage, List<HistoryDTO> historyList)
    {
        ChangeResponse changeResponse = new ChangeResponse();
        changeResponse.setHistoryDTOs(historyList);

        changeResponse.add(linkTo(PhenotypingStageController.class)
                .slash("plans")
                .slash(phenotypingStage.getPhenotypingAttempt().getPlan().getPin())
                .slash("phenotypingStages")
                .slash(phenotypingStage.getPsn()).withSelfRel());
        return changeResponse;
    }

    @PutMapping(value = {"plans/{pin}/phenotypingStages/{psn}"})
    public HistoryDTO update(
            @PathVariable String pin, @PathVariable String psn,
            @RequestBody PhenotypingStageUpdateDTO phenotypingStageUpdateDTO)
    {
        HistoryDTO historyDTO = new HistoryDTO();
        phenotypingStageRequestProcessor.validateAssociation(pin, psn);
        PhenotypingStage phenotypingStage = getPhenotypingStageToUpdate(phenotypingStageUpdateDTO);
        History history = phenotypingStageService.update(phenotypingStage);

        if (history != null)
        {
            historyDTO = historyMapper.toDto(history);
        }
        return historyDTO;
    }

    /**
     * Get a PhenotypingStage object based on PhenotypingStageUpdateDTO using the fields that can be
     * updated by the user.
     * @param phenotypingStageUpdateDTO phenotyping stage sent by the user.
     * @return The original phenotyping stage with the allowed modifications specified in the dto.
     */
    private PhenotypingStage getPhenotypingStageToUpdate(
        PhenotypingStageUpdateDTO phenotypingStageUpdateDTO)
    {
        PhenotypingStage currentPhenotypingStage =
            phenotypingStageService.getByPsnFailsIfNotFound(phenotypingStageUpdateDTO.getPsn());
        return phenotypingStageRequestProcessor.getPhenotypingStageToUpdate(
            currentPhenotypingStage, phenotypingStageUpdateDTO);
    }
}
