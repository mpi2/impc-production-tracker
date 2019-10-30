package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProjectConsortiumDTO {
    private Long id;
    private String consortiumName;
}
