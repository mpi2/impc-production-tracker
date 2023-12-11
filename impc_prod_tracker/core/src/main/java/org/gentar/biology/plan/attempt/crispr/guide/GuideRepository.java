package org.gentar.biology.plan.attempt.crispr.guide;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface GuideRepository extends CrudRepository<Guide, Long> {


    @Transactional
    @Modifying
    @Query(value = "UPDATE guide " +
        "SET gid              = 'GID:' || LPAD(id::text, 12, '0'), " +
        "    last_modified_by = 'mouse-informatics@ebi.ac.uk', " +
        "    last_modified = now() " +
        "where gid is null " +
        "   or gid = '';",
        nativeQuery = true)
    void updateGidForNull();
}
