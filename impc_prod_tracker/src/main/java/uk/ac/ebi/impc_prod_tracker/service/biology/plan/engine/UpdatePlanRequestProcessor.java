package uk.ac.ebi.impc_prod_tracker.service.biology.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production.crispr_attempt.CrisprAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.attempt.crispr_attempt.CrisprAttemptMapper;

/**
 * Class in charge of analysing a PlanDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private CrisprAttemptMapper crisprAttemptMapper;

    public UpdatePlanRequestProcessor(CrisprAttemptMapper crisprAttemptMapper)
    {
        this.crisprAttemptMapper = crisprAttemptMapper;
    }

    /**
     *
     * @param plan An Existing plan in the system.
     * @param planDTO New information for plan.
     * @return The plan object with the new information that is requested in updatePlanRequestDTO.
     * This is only modifications in the object. No database changes here.
     */
    public Plan getPlanToUpdate(Plan plan, PlanDTO planDTO)
    {
        if (planDTO != null)
        {
            updateBasicInformation(plan, planDTO);

            if (planDTO.getCrisprAttemptDTO() != null)
            {
                setNewCrisprAttempt(plan, planDTO.getCrisprAttemptDTO());
            }
        }
        return plan;
    }

    private void setNewCrisprAttempt(Plan plan, CrisprAttemptDTO crisprAttemptDTO)
    {
        crisprAttemptDTO.setCrisprAttemptId(plan.getId());
        CrisprAttempt crisprAttempt = crisprAttemptMapper.toEntity(crisprAttemptDTO);
        crisprAttempt.setImitsMiAttemptId(plan.getCrisprAttempt().getImitsMiAttemptId());
        crisprAttempt.setPlan(plan);
        crisprAttempt.setId(plan.getId());
        plan.setCrisprAttempt(crisprAttempt);
    }

    private void updateBasicInformation(Plan plan, PlanDTO planDTO)
    {
        Boolean newIsActive = planDTO.getIsActive();
        if (newIsActive != null)
        {
            plan.setIsActive(newIsActive);
        }
        String newComment = planDTO.getComment();
        if (newComment != null)
        {
            plan.setComment(newComment);
        }
    }
}
