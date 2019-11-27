package org.gentar.biology.project.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.biology.project.Project;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult
{
    private String input;
    private Project project;
    private String comment;
}
