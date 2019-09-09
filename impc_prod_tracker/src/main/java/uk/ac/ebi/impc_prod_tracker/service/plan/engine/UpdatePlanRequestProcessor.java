package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.PrivacyRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroupRepository;
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
    private PrivacyRepository privacyRepository;
    private WorkGroupRepository workGroupRepository;
    private CrisprAttemptMapper crisprAttemptMapper;

    public UpdatePlanRequestProcessor(
        PrivacyRepository privacyRepository,
        WorkGroupRepository workGroupRepository,
        CrisprAttemptMapper crisprAttemptMapper)
    {
        this.privacyRepository = privacyRepository;
        this.workGroupRepository = workGroupRepository;
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
        // TODO move privacy and work_group to project level
//        String newPrivacyName = planDTO.getPrivacyName();
//        if (newPrivacyName != null)
//        {
//            Privacy newPrivacy = privacyRepository.findByName(newPrivacyName);
//            if (newPrivacy != null)
//            {
//                plan.setPrivacy(newPrivacy);
//            }
//        }
//        // ADMIN
//        String newWorkGroupName = planDTO.getWorkGroupName();
//        if (newWorkGroupName != null)
//        {
//            WorkGroup newWorkGroup = workGroupRepository.findWorkGroupByName(newWorkGroupName);
//            if (newWorkGroup != null)
//            {
//                plan.setWorkGroup(newWorkGroup);
//            }
//        }
        String newComment = planDTO.getComment();
        if (newComment != null)
        {
            plan.setComment(newComment);
        }
    }
}
