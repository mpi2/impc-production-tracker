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
package org.gentar.biology.gene_list;

import org.gentar.biology.gene_list.record.GeneListProjection;
import org.gentar.organization.consortium.Consortium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneListRepository extends JpaRepository<GeneList, Long>
{
    List<GeneList> findAll();
    GeneList findByConsortium(Consortium consortium);
    GeneList findByConsortiumName(String consortiumName);

    //@Query("select lr.id, lr.note, g.symbol, g.accId, p.tpn, a.name as assignmentStatus, privacy.name as privacy, s.name as summaryStatus from Consortium c left join GeneList gl on c = gl.consortium left join ListRecord lr on gl = lr.geneList left join GeneByListRecord gblr on lr = gblr.listRecord left join Gene g on gblr.accId = g.accId left join ProjectIntentionGene pig on g = pig.gene  left join ProjectIntention pi on pig.projectIntention = pi.project left join Project p on pi.project = p left join Status s on p.summaryStatus = s left join Privacy privacy on p.privacy = privacy left join AssignmentStatus a on p.assignmentStatus = a  where c.name = :name" )
    @Query("select distinct lr.id as id, lr.note as note, lr.visible as visible, g.symbol as symbol, g.accId as accId, p.tpn as tpn, a.name as assignmentStatus, priv.name as privacy, s.name as summaryStatus  from Consortium c left join GeneList gl on c = gl.consortium left join ListRecord lr on gl = lr.geneList left join GeneByListRecord gblr on lr = gblr.listRecord left join Gene g on gblr.accId = g.accId left join ProjectIntentionGene pig on g = pig.gene  left join ProjectIntention pi on pig.projectIntention = pi left join Project p on pi.project = p join Status s on p.summaryStatus = s join Privacy priv on p.privacy = priv join AssignmentStatus a on p.assignmentStatus = a  where c.name = :name")
    List<GeneListProjection> findAllGeneListProjectionsByConsortiumName(@Param("name") String consortiumName);

    // <T> T findAllByConsortiumNameForGeneListProjection(String consortiumName, Class<T> type);
}
