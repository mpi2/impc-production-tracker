package org.gentar.biology.plan.attempt.crispr.guide;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GuideRepository extends CrudRepository<Guide, Long> {


    @Transactional
    @Modifying
    @Query(value = "UPDATE guide  " +
            "SET gid = 'GID:' || LPAD(CAST(id AS VARCHAR(12)), 12, '0'), " +
            "    last_modified_by = 'mouse-informatics@ebi.ac.uk', " +
            "    last_modified = NOW() " +
            "WHERE gid IS NULL OR gid = '';",
            nativeQuery = true)
    void updateGidForNull();

    @Query("SELECT g FROM Guide g WHERE NOT EXISTS (" +
            "SELECT 1 FROM MergedGuide mg WHERE " +
            "mg.gid = g.gid AND " +
            "(mg.chr = g.chr OR (mg.chr IS NULL AND g.chr IS NULL)) AND " +
            "(mg.genomeBuild = g.genomeBuild OR (mg.genomeBuild IS NULL AND g.genomeBuild IS NULL)) AND " +
            "(mg.grnaConcentration = g.grnaConcentration OR (mg.grnaConcentration IS NULL AND g.grnaConcentration IS NULL)) AND " +
            "(mg.guideSequence = g.guideSequence OR (mg.guideSequence IS NULL AND g.guideSequence IS NULL)) AND " +
            "(mg.pam = g.pam OR (mg.pam IS NULL AND g.pam IS NULL)) AND " +
            "(mg.sequence = g.sequence OR (mg.sequence IS NULL AND g.sequence IS NULL)) AND " +
            "(mg.start = g.start OR (mg.start IS NULL AND g.start IS NULL)) AND " +
            "(mg.stop = g.stop OR (mg.stop IS NULL AND g.stop IS NULL)) AND " +
            "(mg.strand = g.strand OR (mg.strand IS NULL AND g.strand IS NULL))" +
            ")")
    List<Guide> findNonExistingGuides();


}
