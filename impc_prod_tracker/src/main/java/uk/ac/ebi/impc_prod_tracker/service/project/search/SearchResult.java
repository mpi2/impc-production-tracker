package uk.ac.ebi.impc_prod_tracker.service.project.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult
{
    private String input;
    private Project project;
    private String comment;
}
