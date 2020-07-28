package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.Mapper;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationController;
import org.gentar.biology.mutation.MutationResponseDTO;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.PlanController;
import org.gentar.biology.plan.attempt.phenotyping.stage.status_stamp.PhenotypingStageStatusStamp;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.TransitionEvaluation;
import org.gentar.statemachine.TransitionMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PhenotypingStageResponseMapper implements Mapper<PhenotypingStage, PhenotypingStageResponseDTO>
{
    private final PhenotypingStageCommonMapper phenotypingStageCommonMapper;
    private final PhenotypingStageService phenotypingStageService;
    private final TransitionMapper transitionMapper;

    public PhenotypingStageResponseMapper(
        PhenotypingStageCommonMapper phenotypingStageCommonMapper,
        PhenotypingStageService phenotypingStageService,
        TransitionMapper transitionMapper)
    {
        this.phenotypingStageCommonMapper = phenotypingStageCommonMapper;
        this.phenotypingStageService = phenotypingStageService;
        this.transitionMapper = transitionMapper;
    }

    @Override
    public PhenotypingStageResponseDTO toDto(PhenotypingStage phenotypingStage)
    {
        PhenotypingStageResponseDTO phenotypingStageResponseDTO = new PhenotypingStageResponseDTO();
        if (phenotypingStage != null)
        {
            PhenotypingStageCommonDTO phenotypingStageCommonDTO =
                phenotypingStageCommonMapper.toDto(phenotypingStage);
            phenotypingStageResponseDTO.setStatusName(phenotypingStage.getStatus().getName());
            phenotypingStageResponseDTO.setPhenotypingStageCommonDTO(phenotypingStageCommonDTO);
            phenotypingStageResponseDTO.setId(phenotypingStage.getId());
            phenotypingStageResponseDTO.setPhenotypingExternalRef(
                phenotypingStage.getPhenotypingAttempt().getPhenotypingExternalRef());
            if (phenotypingStage.getPhenotypingAttempt().getPlan() != null)
            {
                phenotypingStageResponseDTO.setPin(
                    phenotypingStage.getPhenotypingAttempt().getPlan().getPin());
            }
            phenotypingStageResponseDTO.setPsn(phenotypingStage.getPsn());
            if (phenotypingStage.getPhenotypingStageType() != null)
            {
                phenotypingStageResponseDTO.setPhenotypingTypeName(
                    phenotypingStage.getPhenotypingStageType().getName());
            }
            addStatusStamps(phenotypingStageResponseDTO, phenotypingStage);
            phenotypingStageResponseDTO.setStatusTransitionDTO(
                buildStatusTransitionDTO(phenotypingStage));
            addSelfLink(phenotypingStageResponseDTO, phenotypingStage);
            addPlanLink(phenotypingStageResponseDTO, phenotypingStage);
        }
        return phenotypingStageResponseDTO;
    }

    private void addStatusStamps(
        PhenotypingStageResponseDTO phenotypingStageResponseDTO, PhenotypingStage phenotypingStage)
    {
        Set<PhenotypingStageStatusStamp> statusStamps =
                phenotypingStage.getPhenotypingStageStatusStamps();
        List<StatusStampsDTO> statusStampsDTOS = new ArrayList<>();
        if (statusStamps != null) {
            statusStamps.forEach(x -> {
                StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
                statusStampsDTO.setStatusName(x.getStatus().getName());
                statusStampsDTO.setDate(x.getDate());
                statusStampsDTOS.add(statusStampsDTO);
            });
        }
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        phenotypingStageResponseDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    private StatusTransitionDTO buildStatusTransitionDTO(PhenotypingStage phenotypingStage)
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        statusTransitionDTO.setCurrentStatus(phenotypingStage.getStatus().getName());
        statusTransitionDTO.setTransitions(getTransitions(phenotypingStage));
        return statusTransitionDTO;
    }

    private List<TransitionDTO> getTransitions(PhenotypingStage phenotypingStage)
    {
        List<TransitionEvaluation> transitionEvaluations =
                phenotypingStageService.evaluateNextTransitions(phenotypingStage);
        return transitionMapper.toDtos(transitionEvaluations);
    }

    private void addSelfLink(
        PhenotypingStageResponseDTO phenotypingStageResponseDTO, PhenotypingStage phenotypingStage)
    {
        Link link = linkTo(methodOn(PhenotypingStageController.class)
            .findOneByPlanAndPsn(
                phenotypingStage.getPhenotypingAttempt().getPlan().getPin(),
                phenotypingStage.getPsn()))
            .withSelfRel();
        phenotypingStageResponseDTO.add(link);
    }

    private void addPlanLink(
        PhenotypingStageResponseDTO phenotypingStageResponseDTO, PhenotypingStage phenotypingStage)
    {
        Link link = linkTo(methodOn(PlanController.class)
            .findOne(phenotypingStage.getPhenotypingAttempt().getPlan().getPin())).withRel("plan");
        phenotypingStageResponseDTO.add(link);
    }

    @Override
    public PhenotypingStage toEntity(PhenotypingStageResponseDTO dto)
    {
        // A PhenotypingStage response does not need to be converted to an entity.
        return null;
    }
}
