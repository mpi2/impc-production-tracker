package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeRepository;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OutcomeServiceImpl implements OutcomeService
{
    private OutcomeRepository outcomeRepository;
    private OutcomeTypeRepository outcomeTypeRepository;
    private OutcomeCreator outcomeCreator;
    private StatusService statusService;
    private OutcomeUpdater outcomeUpdater;
    private ResourceAccessChecker<Outcome> resourceAccessChecker;

    public static final String READ_OUTCOME_ACTION = "READ_OUTCOME";

    public OutcomeServiceImpl(
        OutcomeRepository outcomeRepository,
        OutcomeTypeRepository outcomeTypeRepository,
        OutcomeCreator outcomeCreator,
        StatusService statusService,
        OutcomeUpdater outcomeUpdater,
        ResourceAccessChecker<Outcome> resourceAccessChecker)
    {
        this.outcomeRepository = outcomeRepository;
        this.outcomeTypeRepository = outcomeTypeRepository;
        this.outcomeCreator = outcomeCreator;
        this.statusService = statusService;
        this.outcomeUpdater = outcomeUpdater;
        this.resourceAccessChecker = resourceAccessChecker;
    }

    private List<Outcome> getCheckedCollection(Collection<Outcome> outcomes)
    {
        return outcomes.stream().map(this::getAccessChecked)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Outcome getAccessChecked(Outcome outcome)
    {
        return (Outcome) resourceAccessChecker.checkAccess(outcome, READ_OUTCOME_ACTION);
    }

    public List<Outcome> getOutcomes()
    {
//        return outcomeRepository.findAll();

        return getCheckedCollection(outcomeRepository.findAll());
    }

    public Outcome create(Outcome outcome)
    {
        setInitialStatus(outcome);
        Outcome createdOutcome = outcomeCreator.create(outcome);
        return createdOutcome;
    }

    private void setInitialStatus(Outcome outcome)
    {
        Colony colony = outcome.getColony();
        if (colony != null)
        {
            Status colonyStatus =
                statusService.getStatusByName(ColonyState.GenotypeNotConfirmed.getInternalName());
            colony.setStatus(colonyStatus);
        }
    }

    public History update(Outcome outcome)
    {
        Outcome originalOutcome = new Outcome(getByTpoFailsIfNotFound(outcome.getTpo()));
        return outcomeUpdater.update(originalOutcome, outcome);
    }

    @Override
    public OutcomeType getOutcomeTypeByName(String name)
    {
        return outcomeTypeRepository.findByNameIgnoreCase(name);
    }

    @Override
    public OutcomeType getOutcomeTypeByNameFailingWhenNull(String name)
    {
        OutcomeType outcomeType = getOutcomeTypeByName(name);
        if (outcomeType == null)
        {
            throw new UserOperationFailedException("Outcome type" + name + " does not exist.");
        }
        return outcomeType;
    }

    @Override
    public Outcome getByTpoFailsIfNotFound(String tpo)
    {
        Outcome outcome = outcomeRepository.findByTpo(tpo);
        if (outcome == null)
        {
            throw new UserOperationFailedException("Outocome " + tpo + " does not exist");
        }
        return outcome;
    }
}
