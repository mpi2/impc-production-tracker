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
package org.gentar.biology.project.search.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

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

    private ProjectFilterBuilder withFilter(FilterTypes filterType, List<String> values)
    {
        if (isListValid(values))
        {
            filters.put(filterType, values);
        }
        return this;
    }

    public ProjectFilterBuilder withTpns(List<String> tpns)
    {
        return withFilter(FilterTypes.TPN, tpns);
    }

    public ProjectFilterBuilder withMarkerSymbols(List<String> markerSymbols)
    {
        return withFilter(FilterTypes.MARKER_SYMBOL, markerSymbols);
    }

    public ProjectFilterBuilder withGenes(List<String> genesNameOrIds)
    {
        return withFilter(FilterTypes.GENE, genesNameOrIds);
    }

    public ProjectFilterBuilder withIntentions(List<String> intentions)
    {
        return withFilter(FilterTypes.INTENTION, intentions);
    }

    public ProjectFilterBuilder withPrivacies(List<String> privacies)
    {
        return withFilter(FilterTypes.PRIVACY_NAME, privacies);
    }

    public ProjectFilterBuilder withStatuses(List<String> statuses)
    {
        return withFilter(FilterTypes.ASSIGNMENT_STATUS, statuses);
    }

    public ProjectFilterBuilder withWorkUnitNames(List<String> workUnitNames)
    {
        return withFilter(FilterTypes.WORK_UNIT_NAME, workUnitNames);
    }

    public ProjectFilterBuilder withWorkGroupNames(List<String> workGroupNames)
    {
        return withFilter(FilterTypes.WORK_GROUP_NAME, workGroupNames);
    }

    public ProjectFilterBuilder withConsortiaNames(List<String> consortiaNames)
    {
        return withFilter(FilterTypes.CONSORTIUM, consortiaNames);
    }

    public ProjectFilterBuilder withSummaryStatusNames(List<String> summaryStatusNames)
    {
        return withFilter(FilterTypes.SUMMARY_STATUS, summaryStatusNames);
    }

    public ProjectFilterBuilder withExternalReference(List<String> externalReferences)
    {
        return withFilter(FilterTypes.EXTERNAL_REFERENCE, externalReferences);
    }

    public ProjectFilterBuilder withImitsMiPlanId(List<String> imitsMiPlanIds)
    {
        return withFilter(FilterTypes.IMITS_MI_PLAN, imitsMiPlanIds);
    }

    private boolean isListValid(List<String> values)
    {
        return values != null && !values.isEmpty();
    }
}
