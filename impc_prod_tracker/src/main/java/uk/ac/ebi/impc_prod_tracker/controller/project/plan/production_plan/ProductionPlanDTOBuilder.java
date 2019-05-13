package uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.crispr_attempt_details.CrisprAttemptDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.f1_colony.F1ColonyDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.micro_injection.MicroInjectionDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttemptRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProductionPlanDTOBuilder
{
    private PlanService planService;
    private CrisprAttemptRepository crisprAttemptRepository;

    public ProductionPlanDTOBuilder(
        PlanService planService, CrisprAttemptRepository crisprAttemptRepository)
    {
        this.planService = planService;
        this.crisprAttemptRepository = crisprAttemptRepository;
    }

    public ProductionPlanSummaryDTO buildProductionPlanSummaryDTOFromPlan(final Plan plan)
    {
        ProductionPlanSummaryDTO productionPlanSummaryDTO = new ProductionPlanSummaryDTO();
        productionPlanSummaryDTO.setMicroInjectionDetailsDTO(buildMicroInjectionDetailsDTO(plan));
        productionPlanSummaryDTO.setCrisprAttemptDetailsDTO(buildCrisprAttemptDetailsDTO(plan));
        productionPlanSummaryDTO.setF1ColonyDetailsDTO(buildF1ColonyDetailsDTO(plan));
        return productionPlanSummaryDTO;
    }

    private MicroInjectionDetailsDTO buildMicroInjectionDetailsDTO(final Plan plan)
    {
        MicroInjectionDetailsDTO microInjectionDetailsDTO = new MicroInjectionDetailsDTO();
        microInjectionDetailsDTO.setBlastStrain("Blast Strain");
        microInjectionDetailsDTO.setReference("Reference");
        return microInjectionDetailsDTO;
    }

    private F1ColonyDetailsDTO buildF1ColonyDetailsDTO(final Plan plan)
    {
        F1ColonyDetailsDTO f1ColonyDetailsDTO =  new F1ColonyDetailsDTO();
        f1ColonyDetailsDTO.setAlleleName("Allele Name");
        return f1ColonyDetailsDTO;
    }

    private CrisprAttemptDetailsDTO buildCrisprAttemptDetailsDTO(final Plan plan)
    {
        CrisprAttemptDetailsDTO crisprAttemptDetailsDTO = new CrisprAttemptDetailsDTO();
        Optional<CrisprAttempt> crisprAttemptOpt = crisprAttemptRepository.findById(plan.getId());
        if (crisprAttemptOpt.isPresent())
        {
            CrisprAttempt crisprAttempt = crisprAttemptOpt.get();

            crisprAttemptDetailsDTO.setComments(crisprAttempt.getComment());
            crisprAttemptDetailsDTO.setMiDate(crisprAttempt.getMiDate());
            crisprAttemptDetailsDTO.setMiExternalRef(crisprAttemptDetailsDTO.getMiExternalRef());
            crisprAttemptDetailsDTO.setExperimental(crisprAttempt.getExperimental());
        }
        return crisprAttemptDetailsDTO;
    }
}
