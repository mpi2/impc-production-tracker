package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanService;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.PhenotypingStageState;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeRepository;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.Resource;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PhenotypingStageServiceImpl implements PhenotypingStageService
{
    private PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver;
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;
    private PhenotypingStageRepository phenotypingStageRepository;
    private PhenotypingStageTypeRepository phenotypingStageTypeRepository;
    private PlanService planService;
    private ResourceAccessChecker<PhenotypingStage> resourceAccessChecker;
    private StatusService statusService;
    private PhenotypingStageCreator phenotypingStageCreator;
    private PhenotypingStageUpdater phenotypingStageUpdater;
    private HistoryService historyService;

    public static final String READ_PHENOTYPING_STAGE_ACTION = "READ_PHENOTYPING_STAGE";

    public PhenotypingStageServiceImpl(
            PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver,
            TransitionAvailabilityEvaluator transitionAvailabilityEvaluator,
            PhenotypingStageRepository phenotypingStageRepository,
            PhenotypingStageTypeRepository phenotypingStageTypeRepository,
            PlanService planService,
            ResourceAccessChecker<PhenotypingStage> resourceAccessChecker,
            StatusService statusService,
            PhenotypingStageCreator phenotypingStageCreator,
            PhenotypingStageUpdater phenotypingStageUpdater,
            HistoryService historyService)
    {
        this.phenotypingStageStateMachineResolver = phenotypingStageStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
        this.phenotypingStageRepository = phenotypingStageRepository;
        this.phenotypingStageTypeRepository = phenotypingStageTypeRepository;
        this.planService = planService;
        this.resourceAccessChecker = resourceAccessChecker;
        this.statusService = statusService;
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
    public PhenotypingStage getPhenotypingStageByPinAndPsn(String pin, String psn) {
        Plan plan = planService.getNotNullPlanByPin(pin);
        PhenotypingStage phenotypingStage = getByPsnFailsIfNotFound(psn);
        if (plan != phenotypingStage.getPhenotypingAttempt().getPlan())
        {
            throw new UserOperationFailedException(
                    "Plan "+ pin + " is not related with phenotyping stage +" + psn + ".");
        }
        return phenotypingStage;
    }

    @Override
    public PhenotypingStage getByPsnFailsIfNotFound(String psn)
    {
        PhenotypingStage phenotypingStage = phenotypingStageRepository.findByPsn(psn);
        if (phenotypingStage == null)
        {
            throw new UserOperationFailedException("PhenotypingsStage " + psn + " does not exist");
        }
        return phenotypingStage;
    }

    @Override
    public List<PhenotypingStage> getPhenotypingStages() {
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
        return (PhenotypingStage) resourceAccessChecker.checkAccess((Resource<PhenotypingStage>) phenotypingStage,
                READ_PHENOTYPING_STAGE_ACTION);
    }

    @Override
    public PhenotypingStage create(PhenotypingStage phenotypingStage) {
        setInitialStatus(phenotypingStage);
        PhenotypingStage createdPhenotypingStage = phenotypingStageCreator.create(phenotypingStage);
        return createdPhenotypingStage;
    }

    private void setInitialStatus(PhenotypingStage phenotypingStage)
    {
        Status phenotypingStageStatus = statusService.getStatusByName(PhenotypingStageState.PhenotypingStarted.getInternalName());
        phenotypingStage.setStatus(phenotypingStageStatus);
    }

    @Transactional
    public History update(PhenotypingStage phenotypingStage) {
        PhenotypingStage originalPhenotypingStage = new PhenotypingStage(getByPsnFailsIfNotFound(phenotypingStage.getPsn()));
        return phenotypingStageUpdater.update(originalPhenotypingStage, phenotypingStage);
    }

    @Override
    public List<History> getPhenotypingStageHistory (PhenotypingStage phenotypingStage)
    {
        return historyService.getHistoryByEntityNameAndEntityId("PhenotypingStage", phenotypingStage.getId());
    }

    @Override
    public PhenotypingStageType getPhenotypingStageTypeByNameFailingWhenNull(String name)
    {
        PhenotypingStageType phenotypingStageType = getPhenotypingStageTypeByName(name);
        if (phenotypingStageType == null)
        {
            throw new UserOperationFailedException("Phenotyping sage type" + name + " does not exist.");
        }
        return phenotypingStageType;
    }

    @Override
    public PhenotypingStageType getPhenotypingStageTypeByName(String name)
    {
        return phenotypingStageTypeRepository.findByNameIgnoreCase(name);
    }
}
