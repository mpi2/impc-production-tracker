package org.gentar.biology.plan.engine.crispr_allele_modification;

import java.util.Arrays;
import java.util.List;
import org.gentar.statemachine.EnumStateHelper;
import org.gentar.statemachine.ProcessState;

public enum CrisprAlleleModificationPlanState implements ProcessState {
    PlanCreated("Plan Created"),
    PlanAbandoned("Plan Abandoned"),
    MouseAlleleModificationRegistered("Mouse Allele Modification Registered"),
    RederivationForModificationStarted("Rederivation For Modification Started"),
    RederivationForModificationComplete("Rederivation For Modification Complete"),
    CreExcisionStarted("Cre Excision Started"),
    CreExcisionComplete("Cre Excision Complete"),
    MouseAlleleModificationGenotypeConfirmed("Mouse Allele Modification Genotype Confirmed"),
    MouseAlleleModificationAborted("Mouse Allele Modification Aborted");

    private String internalName;

    CrisprAlleleModificationPlanState(String internalName) {
        this.internalName = internalName;
    }

    public String getInternalName() {
        return internalName;
    }

    public static List<ProcessState> getAllStates() {
        return Arrays.asList(CrisprAlleleModificationPlanState.values());
    }

    public static ProcessState getStateByInternalName(String internalName) {
        return EnumStateHelper.getStateByInternalName(
                Arrays.asList(CrisprAlleleModificationPlanState.values()), internalName);
    }

    @Override
    public String getName() {
        return this.name();
    }

}
