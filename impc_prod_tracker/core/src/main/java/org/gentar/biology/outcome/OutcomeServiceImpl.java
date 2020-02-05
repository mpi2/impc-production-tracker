package org.gentar.biology.outcome;

import org.gentar.audit.history.History;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyState;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OutcomeServiceImpl implements OutcomeService
{
    private OutcomeRepository outcomeRepository;
    private OutcomeTypeRepository outcomeTypeRepository;
    private OutcomeCreator outcomeCreator;
    private StatusService statusService;
    private OutcomeUpdater outcomeUpdater;

    public OutcomeServiceImpl(
        OutcomeRepository outcomeRepository,
        OutcomeTypeRepository outcomeTypeRepository,
        OutcomeCreator outcomeCreator,
        StatusService statusService,
        OutcomeUpdater outcomeUpdater)
    {
        this.outcomeRepository = outcomeRepository;
        this.outcomeTypeRepository = outcomeTypeRepository;
        this.outcomeCreator = outcomeCreator;
        this.statusService = statusService;
        this.outcomeUpdater = outcomeUpdater;
    }

    public List<Outcome> findAll()
    {
        return outcomeRepository.findAll();
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
