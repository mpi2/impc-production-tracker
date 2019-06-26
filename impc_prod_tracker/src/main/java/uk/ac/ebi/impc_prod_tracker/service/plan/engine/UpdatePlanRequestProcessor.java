package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.UpdatePlanRequestDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.ProductionPlanDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.AttemptDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.crispr_attempt.CrisprAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assay_type.AssayType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assay_type.AssayTypeRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.experiment.privacy.PrivacyRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroupRepository;

import java.time.LocalDateTime;

/**
 * Class in charge of analising a UpdatePlanRequestDTO object and retrieve the Plan object
 * intended to be updated.
 */
@Component
public class UpdatePlanRequestProcessor
{
    private PrivacyRepository privacyRepository;
    private WorkGroupRepository workGroupRepository;
    private AssayTypeRepository assayTypeRepository;

    public UpdatePlanRequestProcessor(
        PrivacyRepository privacyRepository, WorkGroupRepository workGroupRepository, AssayTypeRepository assayTypeRepository)
    {
        this.privacyRepository = privacyRepository;
        this.workGroupRepository = workGroupRepository;
        this.assayTypeRepository = assayTypeRepository;
    }

    /**
     *
     * @param plan An Existing plan in the system.
     * @param updatePlanRequestDTO New information for plan.
     * @return The plan object with the new information that is requested in updatePlanRequestDTO.
     * This is only modifications in the object. No database changes here.
     */
    public Plan getPlanToUpdate(Plan plan, UpdatePlanRequestDTO updatePlanRequestDTO)
    {
        PlanDetailsDTO planDetailsDTO = updatePlanRequestDTO.getPlanDetailsDTO();
        if (planDetailsDTO != null)
        {
            updatePlanWithPlanDetailDTO(plan, planDetailsDTO);
        }
        ProductionPlanDTO productionPlanDTO = updatePlanRequestDTO.getProductionPlanDTO();

        if (productionPlanDTO != null)
        {
            AttemptDTO attemptDTO = productionPlanDTO.getAttempt();
            if (attemptDTO != null)
            {
                CrisprAttemptDTO crisprAttemptDTO = attemptDTO.getCrisprAttempt();
                updatePlanWithCrisprAttemptDTO(plan, crisprAttemptDTO);
            }
        }
        return plan;
    }

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

    private void updatePlanWithCrisprAttemptDTO(Plan plan, CrisprAttemptDTO crisprAttemptDTO)
    {
        CrisprAttempt newCrisprAttempt = plan.getCrisprAttempt();
        LocalDateTime newMiDate = crisprAttemptDTO.getMiDate();
        newCrisprAttempt.setMiDate(newMiDate);
        newCrisprAttempt.setMiExternalRef(crisprAttemptDTO.getMiExternalRef());
        newCrisprAttempt.setMutagenesisExternalRef(crisprAttemptDTO.getMutagenesisExternalRef());
        newCrisprAttempt.setIndividuallySetGrnaConcentrations(
            crisprAttemptDTO.getIndividuallySetGrnaConcentrations());
        newCrisprAttempt.setGuidesGeneratedInPlasmid(crisprAttemptDTO.getGuidesGeneratedInPlasmid());
        newCrisprAttempt.setNoG0WhereMutationDetected(crisprAttemptDTO.getNoG0WhereMutationDetected());
        newCrisprAttempt.setNoNhejG0Mutants(crisprAttemptDTO.getNoNhejG0Mutants());
        newCrisprAttempt.setNoDeletionG0Mutants(crisprAttemptDTO.getNoDeletionG0Mutants());
        newCrisprAttempt.setNoHrG0Mutants(crisprAttemptDTO.getNoHrG0Mutants());
        newCrisprAttempt.setNoHdrG0Mutants(crisprAttemptDTO.getNoHdrG0Mutants());
        newCrisprAttempt.setNoHdrG0MutantsAllDonorsInserted(
            crisprAttemptDTO.getNoHdrG0MutantsAllDonorsInserted());
        newCrisprAttempt.setNoHdrG0MutantsSubsetDonorsInserted(
            crisprAttemptDTO.getNoHdrG0MutantsSubsetDonorsInserted());
        newCrisprAttempt.setTotalEmbryosInjected(crisprAttemptDTO.getTotalEmbryosInjected());
        newCrisprAttempt.setTotalEmbryosSurvived(crisprAttemptDTO.getTotalEmbryosSurvived());
        newCrisprAttempt.setTotalTransferred(crisprAttemptDTO.getTotalTransferred());
        newCrisprAttempt.setNoFounderPups(crisprAttemptDTO.getNoFounderPups());
        newCrisprAttempt.setNoFounderSelectedForBreeding(
            crisprAttemptDTO.getNoFounderSelectedForBreeding());
        newCrisprAttempt.setFounderNumAssays(crisprAttemptDTO.getFounderNumAssays());

        AssayType assayType = null;
        if (crisprAttemptDTO.getAssayType() != null)
        {
            assayType = assayTypeRepository.findByName(crisprAttemptDTO.getAssayType());
            if (assayType != null)
            {
                newCrisprAttempt.setAssayType(assayType);
            }
        }
        newCrisprAttempt.setExperimental(crisprAttemptDTO.getExperimental());
        newCrisprAttempt.setEmbryoTransferDay(crisprAttemptDTO.getEmbryoTransferDay());
        newCrisprAttempt.setEmbryo2Cell(crisprAttemptDTO.getEmbryo2Cell());

    }
}
