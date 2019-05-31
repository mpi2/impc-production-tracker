package uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.phenotyping_production;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PhenotypingProductionService;

import java.util.Optional;

@Component
public class PhenotypingProductionDTOBuilder
{
    private PhenotypingProductionService phenotypingProductionService;

    public PhenotypingProductionDTOBuilder(
        PhenotypingProductionService phenotypingProductionService)
    {

        this.phenotypingProductionService = phenotypingProductionService;
    }

    public PhenotypingProductionDTO buildFromPlan(final Plan plan)
    {
        PhenotypingProductionDTO phenotypingProductionDTO = new PhenotypingProductionDTO();
        Optional<PhenotypingProduction> phenotypingProductionOpt =
            phenotypingProductionService.getPhenotypingProductionByPlanId(plan.getId());
        if (phenotypingProductionOpt.isPresent())
        {
            PhenotypingProduction phenotypingProduction = phenotypingProductionOpt.get();
            phenotypingProductionDTO.setColonyName(phenotypingProduction.getPhenotypingColonyName());
            if (phenotypingProduction.getPhenotypingColonyStrain() != null)
            {
                phenotypingProductionDTO.setColonyStrainName(
                    phenotypingProduction.getPhenotypingColonyStrain().getName());
            }
            phenotypingProductionDTO.setExperimentStartedOn(
                phenotypingProduction.getPhenotypingExperimentsStarted());
            phenotypingProductionDTO.setStarted(phenotypingProduction.getPhenotypingStarted());
            phenotypingProductionDTO.setComplete(phenotypingProduction.getPhenotypingStarted());
            phenotypingProductionDTO.setDoNotCountTowardsCompleteness(
                phenotypingProduction.getDoNotCountTowardsCompleteness());
        }
        return phenotypingProductionDTO;
    }
}
