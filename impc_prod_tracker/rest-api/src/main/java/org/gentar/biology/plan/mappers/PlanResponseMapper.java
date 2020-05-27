package org.gentar.biology.plan.mappers;

import org.gentar.Mapper;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanResponseDTO;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.status.StatusStampMapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.common.state_machine.StatusTransitionDTO;
import org.gentar.common.state_machine.TransitionDTO;
import org.gentar.statemachine.TransitionEvaluation;
import org.gentar.statemachine.TransitionMapper;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;

@Component
public class PlanResponseMapper implements Mapper<Plan, PlanResponseDTO>
{
    private PlanCreationMapper planCreationMapper;
    private StatusStampMapper statusStampMapper;
    private PlanService planService;
    private TransitionMapper transitionMapper;

    public PlanResponseMapper(
        PlanCreationMapper planCreationMapper,
        StatusStampMapper statusStampMapper,
        PlanService planService,
        TransitionMapper transitionMapper)
    {
        this.planCreationMapper = planCreationMapper;
        this.statusStampMapper = statusStampMapper;
        this.planService = planService;
        this.transitionMapper = transitionMapper;
    }

    @Override
    public PlanResponseDTO toDto(Plan plan)
    {
        PlanResponseDTO planResponseDTO = new PlanResponseDTO();
        planResponseDTO.setPlanCreationDTO(planCreationMapper.toDto(plan));
        planResponseDTO.setPin(plan.getPin());
        if (plan.getStatus() != null)
        {
            planResponseDTO.setStatusName(plan.getStatus().getName());
        }
        if (plan.getSummaryStatus() != null)
        {
            planResponseDTO.setSummaryStatusName(plan.getSummaryStatus().getName());
        }
        setStatusStampsDTOS(planResponseDTO, plan);
        setSummaryStatusStampsDTOS(planResponseDTO, plan);
        setStatusTransitionDTO(planResponseDTO, plan);
        return planResponseDTO;
    }

    private void setStatusStampsDTOS(PlanResponseDTO planResponseDTO, Plan plan)
    {
        List<StatusStampsDTO> statusStampsDTOS =
            statusStampMapper.toDtos(plan.getPlanStatusStamps());
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        planResponseDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    private void setSummaryStatusStampsDTOS(PlanResponseDTO planResponseDTO, Plan plan)
    {
        List<StatusStampsDTO> statusStampsDTOS =
            statusStampMapper.toDtos(plan.getPlanSummaryStatusStamps());
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        planResponseDTO.setSummaryStatusStampsDTOS(statusStampsDTOS);
    }

    private void setStatusTransitionDTO(PlanResponseDTO planResponseDTO, Plan plan)
    {
        StatusTransitionDTO statusTransitionDTO = new StatusTransitionDTO();
        assert plan.getStatus() != null : "Status is null";
        statusTransitionDTO.setCurrentStatus(plan.getStatus().getName());
        statusTransitionDTO.setTransitions(getTransitionsByPlanType(plan));
        planResponseDTO.setStatusTransitionDTO(statusTransitionDTO);
    }

    private List<TransitionDTO> getTransitionsByPlanType(Plan plan)
    {
        List<TransitionEvaluation> transitionEvaluations =
            planService.evaluateNextTransitions(plan);
        return transitionMapper.toDtos(transitionEvaluations);
    }

    @Override
    public Plan toEntity(PlanResponseDTO planResponseDTO)
    {
        // We don't need to convert a plan response into a entity. The PlanResponseDTO is meant
        // only to show a response: the transformation of a plan entity into a dto that the user
        // can see.
        return null;
    }
}
