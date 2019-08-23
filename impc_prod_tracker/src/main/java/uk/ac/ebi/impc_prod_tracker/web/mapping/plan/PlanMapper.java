package uk.ac.ebi.impc_prod_tracker.web.mapping.plan;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.AttemptMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanMapper
{
    private ModelMapper modelMapper;
    private AttemptMapper attemptMapper;

    public PlanMapper(ModelMapper modelMapper, AttemptMapper attemptMapper)
    {
        this.modelMapper = modelMapper;
        this.attemptMapper = attemptMapper;
    }

    public PlanDTO toDto(Plan plan)
    {
        PlanDTO planDTO = modelMapper.map(plan, PlanDTO.class);
        planDTO.setTpn(plan.getProject().getTpn());
        if (plan.getAttempt() != null)
        {
            planDTO.setAttemptDTO(attemptMapper.toDto(plan.getAttempt()));
        }
        return planDTO;
    }

    public List<PlanDTO> toDtos(List<Plan> plans)
    {
        List<PlanDTO> planDTOS = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(plan -> planDTOS.add(toDto(plan)));
        }
        return planDTOS;
    }
}
