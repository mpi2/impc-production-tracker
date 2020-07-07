package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryMapper;
import org.gentar.biology.ChangeResponse;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.common.history.HistoryDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    private ChangeResponseCreator changeResponseCreator;

    public PhenotypingStageController(
            PhenotypingStageService phenotypingStageService,
            PlanService planService,
            PhenotypingStageRequestProcessor phenotypingStageRequestProcessor,
            HistoryMapper historyMapper,
            PhenotypingStageResponseMapper phenotypingStageResponseMapper,
            PhenotypingStageCreationMapper phenotypingStageCreationMapper, ChangeResponseCreator changeResponseCreator)
    {
        this.phenotypingStageService = phenotypingStageService;
        this.planService = planService;
        this.phenotypingStageRequestProcessor = phenotypingStageRequestProcessor;
        this.historyMapper = historyMapper;
        this.phenotypingStageResponseMapper = phenotypingStageResponseMapper;
        this.phenotypingStageCreationMapper = phenotypingStageCreationMapper;
        this.changeResponseCreator = changeResponseCreator;
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
        List<PhenotypingStageResponseDTO> phenotypingStageResponseDTOS = new ArrayList<>();

        Plan plan = planService.getNotNullPlanByPin(pin);

        if (plan.getPhenotypingAttempt() != null)
        {
            Set<PhenotypingStage> phenotypingStages = plan.getPhenotypingAttempt().getPhenotypingStages();
            phenotypingStageResponseDTOS = phenotypingStageResponseMapper.toDtos(phenotypingStages);
        }
        return phenotypingStageResponseDTOS;
    }

    @GetMapping(value = {"plans/{pin}/phenotypingStages/{psn}/history"})
    public List<HistoryDTO> getPlanHistory(@PathVariable String pin, @PathVariable String psn)
    {
        PhenotypingStage phenotypingStage = phenotypingStageService.getPhenotypingStageByPinAndPsn(pin, psn);

        return historyMapper.toDtos(phenotypingStageService.getPhenotypingStageHistory(phenotypingStage));
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



    @PutMapping(value = {"plans/{pin}/phenotypingStages/{psn}"})
    public ChangeResponse update(@PathVariable String pin, @PathVariable String psn,
            @RequestBody PhenotypingStageUpdateDTO phenotypingStageUpdateDTO)
    {
        phenotypingStageRequestProcessor.validateAssociation(pin, psn);
        PhenotypingStage phenotypingStage = getPhenotypingStageToUpdate(psn, phenotypingStageUpdateDTO);
        History history = phenotypingStageService.update(phenotypingStage);

        return buildChangeResponse(pin, psn, history);
    }

    /**
     * Get a PhenotypingStage object based on PhenotypingStageUpdateDTO using the fields that can be
     * updated by the user.
     * @param phenotypingStageUpdateDTO phenotyping stage sent by the user.
     * @return The original phenotyping stage with the allowed modifications specified in the dto.
     */
    private PhenotypingStage getPhenotypingStageToUpdate(String psn,
                                                         PhenotypingStageUpdateDTO phenotypingStageUpdateDTO)
    {
        PhenotypingStage currentPhenotypingStage = phenotypingStageService.getByPsnFailsIfNotFound(psn);
        return phenotypingStageRequestProcessor.getPhenotypingStageToUpdate(currentPhenotypingStage,
                phenotypingStageUpdateDTO);
    }

    private ChangeResponse buildChangeResponse(PhenotypingStage phenotypingStage)
    {
        List<HistoryDTO> historyList =
                historyMapper.toDtos(phenotypingStageService.getPhenotypingStageHistory(phenotypingStage));
        return buildChangeResponse(phenotypingStage, historyList);
    }
    private ChangeResponse buildChangeResponse(PhenotypingStage phenotypingStage, List<HistoryDTO> historyList)
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

    private ChangeResponse buildChangeResponse(String pin, String psn, History history)
    {
        Link link = buildPhenotypingStageLink(pin, psn);
        return changeResponseCreator.create(link, history);
    }

    private Link buildPhenotypingStageLink(String pin, String psn)
    {
        return linkTo(methodOn(PhenotypingStageController.class).findOneByPlanAndPsn(pin, psn)).withSelfRel();
    }
}
