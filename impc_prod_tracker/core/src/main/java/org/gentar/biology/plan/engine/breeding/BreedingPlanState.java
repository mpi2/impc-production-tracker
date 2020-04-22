package org.gentar.biology.plan.engine.breeding;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum BreedingPlanState implements ProcessState {
    PlanCreated("Plan Created"),
    BreedingStarted("Breeding Started"),
    BreedingComplete("Breeding Complete"),
    BreedingAborted("Breeding Aborted");

    private String internalName;

    BreedingPlanState(String internalName) {
        this.internalName = internalName;
    }

    public String getInternalName() {
        return internalName;
    }

    public static List<ProcessState> getAllStates() {
        return Arrays.asList(BreedingPlanState.values());
    }

    public static ProcessState getStateByInternalName(String internalName) {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(BreedingPlanState.values()), internalName);
    }

    @Override
    public String getName() {
        return this.name();
    }

}
