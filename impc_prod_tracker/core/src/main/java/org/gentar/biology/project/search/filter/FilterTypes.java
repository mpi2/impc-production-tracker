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

import lombok.Getter;

@Getter
public enum FilterTypes
{
    TPN("tpn"),
    GENE("gene"),
    ACC_ID("acc_id"),
    VISIBLE("visible"),
    WORK_UNIT_NAME("workUnitName"),
    WORK_GROUP_NAME("workGroup"),
    MARKER_SYMBOL("markerSymbol"),
    INTENTION("intention"),
    CONSORTIUM("consortium"),
    ASSIGNMENT("assignmentStatus"),
    SUMMARY_STATUS("summaryStatus"),
    PRIVACY_NAME("privacyName"),
    IMITS_MI_PLAN("imitMiPlan"),
    PRODUCTION_COLONY_NAME("productionColonyNames"),
    PHENOTYPING_EXTERNAL_REF("phenotypingExternalRefs");

    private final String name;

    FilterTypes(String name)
    {
        this.name = name;
    }

}
