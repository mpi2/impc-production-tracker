/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package uk.ac.ebi.impc_prod_tracker.web.controller.project.helper;

import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectFilterBuilder
{
    private Map<FilterTypes, List<String>> filters;

    // Prevents instantiation.
    private ProjectFilterBuilder()
    {
        filters = new HashMap<>();
    }

    public static ProjectFilterBuilder getInstance()
    {
        return new ProjectFilterBuilder();
    }

    public ProjectFilter build()
    {
        ProjectFilter projectFilter = new ProjectFilter();
        projectFilter.setFilters(filters);
        return projectFilter;
    }

    public ProjectFilterBuilder withTpns(List<String> tpns)
    {
        filters.put(FilterTypes.TPN, tpns);
        return this;
    }

    public ProjectFilterBuilder withMarkerSymbols(List<String> markerSymbols)
    {
        filters.put(FilterTypes.MARKER_SYMBOL, markerSymbols);
        return this;
    }

    public ProjectFilterBuilder withGenes(List<String> genesNameOrIds)
    {
        filters.put(FilterTypes.GENE, genesNameOrIds);
        return this;
    }

    public ProjectFilterBuilder withIntentions(List<String> intentions)
    {
        filters.put(FilterTypes.INTENTION, intentions);
        return this;
    }

    public ProjectFilterBuilder withPrivacies(List<String> privacies)
    {
        filters.put(FilterTypes.PRIVACY_NAME, privacies);
        return this;
    }

    public ProjectFilterBuilder withStatuses(List<String> statuses)
    {
        filters.put(FilterTypes.ASSIGNMENT_STATUS, statuses);
        return this;
    }

    public ProjectFilterBuilder withWorkUnitNames(List<String> workUnitNames)
    {
        filters.put(FilterTypes.WORK_UNIT_NAME, workUnitNames);
        return this;
    }

    public ProjectFilterBuilder withWorkGroupNames(List<String> workGroupNames)
    {
        filters.put(FilterTypes.WORK_GROUP_NAME, workGroupNames);
        return this;
    }
}
