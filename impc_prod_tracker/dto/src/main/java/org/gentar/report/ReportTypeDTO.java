package org.gentar.report;

import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "reportTypes")
@Data
public class ReportTypeDTO {
    private String name;
    private String description;
}
