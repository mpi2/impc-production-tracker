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
package uk.ac.ebi.impc_prod_tracker.service.biology.project.search;

import java.util.List;

interface SearchExecutor
{
    /**
     * Finds the projects associated with an input (a gene symbol for instance)
     * @param input It depends on the type of search. It can be a gene symbol for instance.
     * @return List of {@link SearchResult} with the results.
     */
    List<SearchResult> findProjects(String input);
}
