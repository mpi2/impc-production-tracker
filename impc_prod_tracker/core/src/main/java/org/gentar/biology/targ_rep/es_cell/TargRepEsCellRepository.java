package org.gentar.biology.targ_rep.es_cell;

import org.springframework.data.repository.CrudRepository;

public interface TargRepEsCellRepository extends CrudRepository<TargRepEsCell, Long>
{
    TargRepEsCell findTargRepEsCellById(Long id);

    TargRepEsCell findTargRepEsCellByName(String name);
}
