package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedType;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedTypeNames;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedTypeService;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
/**
 * This processor is to use with transitions which final status is "Phenotyping Started" where a
 * tissue distribution record must be created given that the work unit is registered to have this
 * behaviour. As other processors that move to "Phenotyping Started", this processor needs to
 * validate that the user has the permissions to do so (currently DCC).
 */
public class PhenotypingStartedProcessorWithTissueCreation extends AbstractProcessor
{
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final MaterialDepositedTypeService materialDepositedTypeService;

    public PhenotypingStartedProcessorWithTissueCreation(
        PhenotypingStageStateSetter phenotypingStageStateSetter,
        ContextAwarePolicyEnforcement policyEnforcement,
        MaterialDepositedTypeService materialDepositedTypeService)
    {
        super(phenotypingStageStateSetter);
        this.policyEnforcement = policyEnforcement;
        this.materialDepositedTypeService = materialDepositedTypeService;
    }

    @Override
    public ProcessData process(ProcessData entity)
    {
        super.process(entity);
        addAutomaticTissueDistributionIfNeeded((PhenotypingStage) entity);
        return entity;
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean validTransition = canExecuteTransition();
        transitionEvaluation.setExecutable(validTransition);
        if (!validTransition)
        {
            transitionEvaluation.setNote("The current user does not have permission to move " +
                "to 'Phenotyping Started'");
        }
        return transitionEvaluation;
    }

    private boolean canExecuteTransition()
    {
        return policyEnforcement.hasPermission(null, Actions.UPDATE_TO_PHENOTYPING_STARTED);
    }

    private void addAutomaticTissueDistributionIfNeeded(PhenotypingStage phenotypingStage)
    {
        if (needToCreateTissueDistribution(phenotypingStage))
        {
            Set<TissueDistribution> tissueDistributions = phenotypingStage.getTissueDistributions();
            List<TissueDistribution> fixedTissueDistributions =
                getFixedTissueDistribution(tissueDistributions);
            if (fixedTissueDistributions.isEmpty())
            {
                addFixedTissueDistribution(phenotypingStage);
            }
            else
            {
                updateFixedTissueDistributions(fixedTissueDistributions);
            }
        }
    }

    private void addFixedTissueDistribution(PhenotypingStage phenotypingStage)
    {
        TissueDistribution tissueDistribution = new TissueDistribution();
        MaterialDepositedType materialDepositedType =
            materialDepositedTypeService.getMaterialDepositedTypeByName(
                MaterialDepositedTypeNames.FIXED_TISSUE.getLabel());
        tissueDistribution.setMaterialDepositedType(materialDepositedType);
        tissueDistribution.setStartDate(LocalDate.now());
        tissueDistribution.setWorkUnit(phenotypingStage.getPhenotypingAttempt().getPlan().getWorkUnit());
        phenotypingStage.addTissueDistribution(tissueDistribution);
    }

    private void updateFixedTissueDistributions(List<TissueDistribution> fixedTissueDistributions)
    {
        fixedTissueDistributions.forEach(x -> x.setEndDate(null));
    }

    private List<TissueDistribution> getFixedTissueDistribution(Set<TissueDistribution> tissueDistributions)
    {
        List<TissueDistribution> fixedTissueDistributions = new ArrayList<>();
        if (tissueDistributions != null)
        {
            fixedTissueDistributions =
                tissueDistributions.stream().filter(this::isFixedTissue).collect(Collectors.toList());
        }
        return fixedTissueDistributions;
    }

    private boolean isFixedTissue(TissueDistribution tissueDistribution)
    {
        boolean result = false;
        MaterialDepositedType materialDepositedType = tissueDistribution.getMaterialDepositedType();
        if (materialDepositedType != null)
        {
            result = MaterialDepositedTypeNames.FIXED_TISSUE.getLabel()
                .equals(materialDepositedType.getName());
        }
        return result;
    }

    private boolean needToCreateTissueDistribution(PhenotypingStage phenotypingStage)
    {
        return policyEnforcement.hasPermission(
            phenotypingStage, Actions.AUTOMATIC_TISSUE_DISTRIBUTION_CREATION);
    }
}
