package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.PrivacyRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroupRepository;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.attempt.production.crispr_attempt.CrisprAttemptDTO;

/**
 * Class in charge of analising a PlanDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private PrivacyRepository privacyRepository;
    private WorkGroupRepository workGroupRepository;
//    private CrisprAttemptDTOBuilder crisprAttemptDTOBuilder;

    public UpdatePlanRequestProcessor(
        PrivacyRepository privacyRepository,
        WorkGroupRepository workGroupRepository)
//        CrisprAttemptDTOBuilder crisprAttemptDTOBuilder)
    {
        this.privacyRepository = privacyRepository;
        this.workGroupRepository = workGroupRepository;
//        this.crisprAttemptDTOBuilder = crisprAttemptDTOBuilder;
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
        }
//        ProductionPlanDTO productionPlanDTO = updatePlanRequestDTO.getProductionPlanDTO();
//
//        if (productionPlanDTO != null)
//        {
//            AttemptDTO attemptDTO = productionPlanDTO.getAttempt();
//            if (attemptDTO != null)
//            {
//                CrisprAttemptDTO crisprAttemptDTO = attemptDTO.getCrisprAttempt();
//                updatePlanWithCrisprAttemptDTO(plan, crisprAttemptDTO);
//            }
//        }
        return plan;
    }

    private void updateBasicInformation(Plan plan, PlanDTO planDTO)
    {
        String newPrivacyName = planDTO.getPrivacyName();
        if (newPrivacyName != null)
        {
            Privacy newPrivacy = privacyRepository.findByName(newPrivacyName);
            if (newPrivacy != null)
            {
                plan.setPrivacy(newPrivacy);
            }
        }
        // ADMIN
        String newWorkGroupName = planDTO.getWorkGroupName();
        if (newWorkGroupName != null)
        {
            WorkGroup newWorkGroup = workGroupRepository.findWorkGroupByName(newWorkGroupName);
            if (newWorkGroup != null)
            {
                plan.setWorkGroup(newWorkGroup);
            }
        }
        String newComment = planDTO.getComment();
        if (newComment != null)
        {
            plan.setComment(newComment);
        }
    }

    private void updatePlanWithCrisprAttemptDTO(Plan plan, CrisprAttemptDTO crisprAttemptDTO)
    {
//        CrisprAttempt crisprAttempt = crisprAttemptDTOBuilder.convertToEntity(crisprAttemptDTO);
//        crisprAttempt.setImitsMiAttemptId(plan.getAttempt().getCrisprAttempt().getImitsMiAttemptId());
//        crisprAttempt.getAttempt().setPlan(plan);
//        crisprAttempt.setId(plan.getId());
//
//        plan.getAttempt().setCrisprAttempt(crisprAttempt);
    }
}
