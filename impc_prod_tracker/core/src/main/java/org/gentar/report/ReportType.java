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
public class ReportType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "reportTypeSeq", sequenceName = "REPORT_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportTypeSeq")
    private Long id;

    @NotNull
    private String name;

    private String description;

}
