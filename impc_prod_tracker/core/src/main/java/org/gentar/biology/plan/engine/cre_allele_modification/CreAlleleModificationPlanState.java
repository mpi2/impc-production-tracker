package org.gentar.biology.plan.engine.cre_allele_modification;

import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

import java.util.Arrays;
import java.util.List;

public enum CreAlleleModificationPlanState implements ProcessState {
    PlanCreated("Plan Created"),
    PlanAbandoned("Plan Abandoned"),
    MouseAlleleModificationRegistered("Mouse Allele Modification Registered"),
    RederivationForModificationStarted("Rederivation For Modification Started"),
    RederivationForModificationComplete("Rederivation For Modification Complete"),
    CreExcisionStarted("Cre Excision Started"),
    CreExcisionComplete("Cre Excision Complete"),
    MouseAlleleModificationAborted("Mouse Allele Modification Aborted");

    private String internalName;

    CreAlleleModificationPlanState(String internalName) {
        this.internalName = internalName;
    }

    public String getInternalName() {
        return internalName;
    }

    public static List<ProcessState> getAllStates() {
        return Arrays.asList(CreAlleleModificationPlanState.values());
    }

    public static ProcessState getStateByInternalName(String internalName) {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(CreAlleleModificationPlanState.values()), internalName);
    }

    @Override
    public String getName() {
        return this.name();
    }

}
