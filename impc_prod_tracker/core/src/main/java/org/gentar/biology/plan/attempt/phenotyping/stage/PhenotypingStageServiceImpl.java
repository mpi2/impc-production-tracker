package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeRepository;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.Resource;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PhenotypingStageServiceImpl implements PhenotypingStageService
{
    private final PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver;
    private final TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;
    private final PhenotypingStageRepository phenotypingStageRepository;
    private final PhenotypingStageTypeRepository phenotypingStageTypeRepository;
    private final PlanService planService;
    private final ResourceAccessChecker<PhenotypingStage> resourceAccessChecker;
    private final PhenotypingStageCreator phenotypingStageCreator;
    private final PhenotypingStageUpdater phenotypingStageUpdater;
    private final HistoryService<PhenotypingStage> historyService;

    public static final String READ_PHENOTYPING_STAGE_ACTION = "READ_PHENOTYPING_STAGE";

    public PhenotypingStageServiceImpl(
        PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator,
        PhenotypingStageRepository phenotypingStageRepository,
        PhenotypingStageTypeRepository phenotypingStageTypeRepository,
        PlanService planService,
        ResourceAccessChecker<PhenotypingStage> resourceAccessChecker,
        PhenotypingStageCreator phenotypingStageCreator,
        PhenotypingStageUpdater phenotypingStageUpdater,
        HistoryService<PhenotypingStage> historyService)
    {
        this.phenotypingStageStateMachineResolver = phenotypingStageStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
        this.phenotypingStageRepository = phenotypingStageRepository;
        this.phenotypingStageTypeRepository = phenotypingStageTypeRepository;
        this.planService = planService;
        this.resourceAccessChecker = resourceAccessChecker;
        this.phenotypingStageCreator = phenotypingStageCreator;
        this.phenotypingStageUpdater = phenotypingStageUpdater;
        this.historyService = historyService;
    }

    @Override
    public List<TransitionEvaluation> evaluateNextTransitions(PhenotypingStage phenotypingStage)
    {
        List<ProcessEvent> transitions =
            phenotypingStageStateMachineResolver.getAvailableTransitionsByEntityStatus(phenotypingStage);
        return transitionAvailabilityEvaluator.evaluate(transitions, phenotypingStage);
    }

    @Override
    public ProcessEvent getProcessEventByName(PhenotypingStage phenotypingStage, String name)
    {
        return phenotypingStageStateMachineResolver.getProcessEventByActionName(
            phenotypingStage, name);
    }

    @Override
    public PhenotypingStage getPhenotypingStageByPinAndPsn(String pin, String psn)
    {
        Plan plan = planService.getNotNullPlanByPin(pin);
        PhenotypingStage phenotypingStage = getByPsnFailsIfNotFound(psn);
        if (plan != phenotypingStage.getPhenotypingAttempt().getPlan())
        {
            throw new UserOperationFailedException(
                "Plan " + pin + " is not related with phenotyping stage +" + psn + ".");
        }
        return phenotypingStage;
    }

    @Override
    public PhenotypingStage getByPsnFailsIfNotFound(String psn)
    {
        PhenotypingStage phenotypingStage = phenotypingStageRepository.findByPsn(psn);
        if (phenotypingStage == null)
        {
            throw new UserOperationFailedException("PhenotypingsStage " + psn + " does not exist.");
        }
        return phenotypingStage;
    }

    @Override
    public List<PhenotypingStage> getPhenotypingStages()
    {
        return getCheckedCollection(phenotypingStageRepository.findAll());
    }

    private List<PhenotypingStage> getCheckedCollection(Collection<PhenotypingStage> phenotypingStages)
    {
        return phenotypingStages.stream().map(this::getAccessChecked)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private PhenotypingStage getAccessChecked(PhenotypingStage phenotypingStage)
    {
        return (PhenotypingStage) resourceAccessChecker.checkAccess(
            (Resource<PhenotypingStage>) phenotypingStage, READ_PHENOTYPING_STAGE_ACTION);
    }

    @Override
    @Transactional
    public PhenotypingStage create(PhenotypingStage phenotypingStage)
    {
        return phenotypingStageCreator.create(phenotypingStage);
    }

    @Transactional
    public History update(PhenotypingStage phenotypingStage)
    {
        PhenotypingStage originalPhenotypingStage =
            new PhenotypingStage(getByPsnFailsIfNotFound(phenotypingStage.getPsn()));
        return phenotypingStageUpdater.update(originalPhenotypingStage, phenotypingStage);
    }

    @Override
    public List<History> getPhenotypingStageHistory(PhenotypingStage phenotypingStage)
    {
        return historyService.getHistoryByEntityNameAndEntityId(
            "PhenotypingStage", phenotypingStage.getId());
    }

    @Override
    public PhenotypingStageType getPhenotypingStageTypeByNameFailingWhenNull(String name)
    {
        PhenotypingStageType phenotypingStageType = getPhenotypingStageTypeByName(name);
        if (phenotypingStageType == null)
        {
            throw new UserOperationFailedException(
                "Phenotyping stage type" + name + " does not exist.");
        }
        return phenotypingStageType;
    }

    @Override
    public PhenotypingStageType getPhenotypingStageTypeByName(String name)
    {
        return phenotypingStageTypeRepository.findByNameIgnoreCase(name);
    }
}
