/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.colony;

import org.gentar.security.abac.subject.SubjectRetriever;
import org.gentar.security.abac.subject.SystemSubject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ColonyController {

    private final ColonyService colonyService;

    private final SubjectRetriever subjectRetriever;
    public ColonyController(
            ColonyService colonyService, SubjectRetriever subjectRetriever) {
        this.colonyService = colonyService;
        this.subjectRetriever = subjectRetriever;
    }

    @GetMapping(value = {"/colony"})
    public ResponseEntity<ColonyRrIdRecord> getGeneSymbols(@RequestParam String name) {

        SystemSubject systemSubject =subjectRetriever.getSubject();

        Colony colony = colonyService.getColonyByColonyName(name);

        if (colony == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        boolean canUpdatePlan = systemSubject.isAdmin() || systemSubject.getPerson().getEmail().equals("jraller+apins@ucdavis.edu");
        return canUpdatePlan?
                ResponseEntity.ok(new ColonyRrIdRecord(colony.getName(), colony.getOutcome().getPlan().getPin(), colony.getOutcome().getTpo())):
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


}
