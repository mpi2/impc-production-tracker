package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.PrivacyRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroupRepository;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan.attempt.crispr_attempt.CrisprAttemptDTOBuilder;

/**
 * Class in charge of analising a UpdatePlanRequestDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private PrivacyRepository privacyRepository;
    private WorkGroupRepository workGroupRepository;
    private CrisprAttemptDTOBuilder crisprAttemptDTOBuilder;

    public UpdatePlanRequestProcessor(
        PrivacyRepository privacyRepository,
        WorkGroupRepository workGroupRepository,
        CrisprAttemptDTOBuilder crisprAttemptDTOBuilder)
    {
        this.privacyRepository = privacyRepository;
        this.workGroupRepository = workGroupRepository;
        this.crisprAttemptDTOBuilder = crisprAttemptDTOBuilder;
    }

    /**
     *
     * @param plan An Existing plan in the system.
     * @param updatePlanRequestDTO New information for plan.
     * @return The plan object with the new information that is requested in updatePlanRequestDTO.
     * This is only modifications in the object. No database changes here.
     */
//    public Plan getPlanToUpdate(Plan plan, UpdatePlanRequestDTO updatePlanRequestDTO)
//    {
//        PlanDetailsDTO planDetailsDTO = updatePlanRequestDTO.getPlanDetailsDTO();
//        if (planDetailsDTO != null)
//        {
//            updatePlanWithPlanDetailDTO(plan, planDetailsDTO);
//        }
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
//        return plan;
//    }

    private void updatePlanWithPlanDetailDTO(Plan plan, PlanDetailsDTO planDetailsDTO)
    {
        String newPrivacyName = planDetailsDTO.getPrivacyName();
        if (newPrivacyName != null)
        {
            Privacy newPrivacy = privacyRepository.findByName(newPrivacyName);
            if (newPrivacy != null)
            {
                plan.setPrivacy(newPrivacy);
            }
        }
        // ADMIN
        String newWorkGroupName = planDetailsDTO.getWorkGroupName();
        if (newWorkGroupName != null)
        {
            WorkGroup newWorkGroup = workGroupRepository.findWorkGroupByName(newWorkGroupName);
            if (newWorkGroup != null)
            {
                plan.setWorkGroup(newWorkGroup);
            }
        }
        String newComments = planDetailsDTO.getComments();
        if (newComments != null)
        {
            plan.setComment(newComments);
        }
    }

//    private void updatePlanWithCrisprAttemptDTO(Plan plan, CrisprAttemptDTO crisprAttemptDTO)
//    {
//        CrisprAttempt crisprAttempt = crisprAttemptDTOBuilder.convertToEntity(crisprAttemptDTO);
//        crisprAttempt.setImitsMiAttemptId(plan.getCrisprAttempt().getImitsMiAttemptId());
//        crisprAttempt.setPlan(plan);
//        crisprAttempt.setId(plan.getCrisprAttempt().getId());
//
//        plan.setCrisprAttempt(crisprAttempt);
//    }
}
