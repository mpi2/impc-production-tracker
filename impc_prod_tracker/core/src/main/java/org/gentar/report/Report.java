package org.gentar.report;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Report extends BaseEntity {

    @Id
    @SequenceGenerator(name = "reportSeq", sequenceName = "REPORT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportSeq")
    private Long id;

    @Column(columnDefinition="TEXT")
    private String report;

    @ManyToOne(targetEntity= ReportType.class, fetch = FetchType.LAZY)
    @NotNull
    private ReportType reportType;

}