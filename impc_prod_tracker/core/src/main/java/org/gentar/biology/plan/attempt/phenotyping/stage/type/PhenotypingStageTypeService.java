package org.gentar.biology.plan.attempt.phenotyping.stage.type;

import org.gentar.biology.plan.attempt.AttemptTypesName;

import java.util.List;
import java.util.Map;

public interface PhenotypingStageTypeService
{
    PhenotypingStageType getPhenotypingStageTypeByName(String name);

    List<PhenotypingStageType> getAll();

    /**
     * Get the phenotyping stage type names associate with a particular attempt type name.
     * @param attemptTypesName attempt type name
     * @return list of phenotyping stage type names associated to the attempt type name.
     */
    List<PhenotypingStageTypeName> getPhenotypingStageTypeNamesByAttemptTypeName(
        AttemptTypesName attemptTypesName);

    Map<String, List<String>> getPhenotypingStageTypeNamesByAttemptTypeNamesMap();
}
