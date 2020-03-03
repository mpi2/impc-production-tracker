package org.gentar.biology.ortholog;

import java.util.List;
import java.util.Map;

public interface OrthologService
{
    Map<String, List<Ortholog>> getOrthologsByAccIds(List<String> accIds);

    List<Ortholog> calculateBestOrthologs(List<Ortholog> orthologs);
}
