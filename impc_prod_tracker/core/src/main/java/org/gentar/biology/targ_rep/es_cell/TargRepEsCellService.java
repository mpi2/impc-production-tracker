package org.gentar.biology.targ_rep.es_cell;

public interface TargRepEsCellService
{
    TargRepEsCell getTargRepEsCellById(Long id);

    TargRepEsCell getTargRepEsCellByNameFailsIfNull(String name);
}
