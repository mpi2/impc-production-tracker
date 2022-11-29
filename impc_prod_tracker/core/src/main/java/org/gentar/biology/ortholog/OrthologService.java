package org.gentar.biology.ortholog;

import java.util.List;
import java.util.Map;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadOrthologDto;

public interface OrthologService {
    Map<String, List<Ortholog>> getOrthologsByAccIds(List<String> accIds);

    Map<String, List<Ortholog>> formatOrthologs(List<ProjectSearchDownloadOrthologDto> projectSearchDownloadOrthologDto);

    List<Ortholog> calculateBestOrthologs(List<Ortholog> orthologs);

    List<ProjectSearchDownloadOrthologDto> getOrthologs(List<String> mgiIds);
}
