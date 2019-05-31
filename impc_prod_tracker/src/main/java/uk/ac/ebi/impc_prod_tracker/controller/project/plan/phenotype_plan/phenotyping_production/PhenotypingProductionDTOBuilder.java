package uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.phenotyping_production;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production.PhenotypingProduction;
import uk.ac.ebi.impc_prod_tracker.data.biology.tissue_distribution_centre.TissueDistributionCentre;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PhenotypingProductionService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            phenotypingProductionService.getPhenotypingProductionByPlan(plan);

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

            List<TissueDistributionCentre> tissueDistributionCentreList =
                phenotypingProductionService.getTissueDistributionCentresByPhenotypingProduction(
                    phenotypingProduction);

            List<TissueDistributionCentreDTO> tissueDistributionCentreDTOS =
                tissueDistributionCentreList.stream().map(
                    p -> {
                        TissueDistributionCentreDTO tissueDistributionCentreDTO =
                            new TissueDistributionCentreDTO();
                        tissueDistributionCentreDTO.setStartDate(p.getStartDate());
                        tissueDistributionCentreDTO.setEndDate(p.getEndDate());
                        if (p.getDistributionCentre() != null)
                        {
                            tissueDistributionCentreDTO.setCentreName(
                                p.getDistributionCentre().getName());
                        }
                        if (p.getMaterialType() != null)
                        {
                            tissueDistributionCentreDTO.setMaterialTypeName(
                                p.getMaterialType().getName());
                        }

                        return tissueDistributionCentreDTO;

                    }).collect(Collectors.toList());
            phenotypingProductionDTO.setTissueDistributionCentreDTOs(tissueDistributionCentreDTOS);

        }
        return phenotypingProductionDTO;
    }
}
