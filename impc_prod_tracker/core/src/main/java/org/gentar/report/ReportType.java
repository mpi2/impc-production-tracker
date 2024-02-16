package org.gentar.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ReportType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "reportTypeSeq", sequenceName = "REPORT_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportTypeSeq")
    private Long id;

    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition = "boolean default true")
    private Boolean isPublic;

}
