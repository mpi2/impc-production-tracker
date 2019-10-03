package uk.ac.ebi.impc_prod_tracker.web.dto.project.search;

import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;

@Data
public class SearchResultDTO
{
    private String input;
    private ProjectDTO project;
    private String comment;
}
