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
package org.gentar.biology.gene_list.record;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListRecordRepository extends CrudRepository<ListRecord, Long>,
    JpaSpecificationExecutor<ListRecord>
{
    Page<ListRecord> findAllByGeneListConsortiumName(Pageable pageable, String consortiumName);

    @Query(
        value = "select * from List_record r " +
            "where r.gene_list_id = ?1 " +
            "and r.id not in(" +
            "    select r_sub.id" +
            "    from List_record r_sub, List_record_List_record_type rel, List_record_type r_type" +
            "    where r_sub.gene_list_id = ?1 and r_sub.id = rel.list_record_id and r_type.id = rel.list_record_type_id" +
            "    and (r_type is null or r_type.visible = false))",
        nativeQuery = true)
    Page<ListRecord> findPublicByConsortiumId(Pageable pageable, Long consortiumId);
}
