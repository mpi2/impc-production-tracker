package uk.ac.ebi.impc_prod_tracker.web.mapping.plan;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.attempt_type.AttemptType;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt.CrisprAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt.CrisprAttemptMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanMapper
{
    private ModelMapper modelMapper;
    private CrisprAttemptMapper crisprAttemptMapper;

    private static final String CRISPR_ATTEMPT_TYPE = "Crispr";


    public PlanMapper(ModelMapper modelMapper, CrisprAttemptMapper crisprAttemptMapper)
    {
        this.modelMapper = modelMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
    }

    public PlanDTO toDto(Plan plan)
    {
        PlanDTO planDTO = modelMapper.map(plan, PlanDTO.class);
        addNoMappedData(planDTO, plan);

        return planDTO;
    }

    private void addNoMappedData(PlanDTO planDTO, Plan plan)
    {
        addAttempt(planDTO, plan);
        planDTO.setTpn(plan.getProject().getTpn());
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

    private void addAttempt(PlanDTO planDTO, Plan plan)
    {
        AttemptType attemptType = plan.getAttemptType();
        String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();

        if (CRISPR_ATTEMPT_TYPE.equalsIgnoreCase(attemptTypeName))
        {
            CrisprAttemptDTO crisprAttemptDTO =
                crisprAttemptMapper.toDto(plan.getCrisprAttempt());
            planDTO.setCrisprAttemptDTO(crisprAttemptDTO);
        } else
        {
            //TODO: other attempts
        }

    }
}
