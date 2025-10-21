package org.gentar.biology.insertion_sequence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface InsertionSequenceRepository extends CrudRepository<InsertionSequence, Long>
{


    InsertionSequence findFirstById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE insertion_sequence\n" +
            "            SET ins = 'INS:' || LPAD(CAST(id AS VARCHAR(12)), 12, '0'),\n" +
            "               last_modified_by = 'mouse-informatics@ebi.ac.uk',\n" +
            "               last_modified = NOW()\n" +
            "            WHERE ins IS NULL OR ins like  '%null%';;",
            nativeQuery = true)
    void updateInsIdForNull();
}
