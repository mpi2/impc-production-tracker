package org.gentar.biology.targ_rep.es_cell;

import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Primary
public interface TargRepEsCellRepository extends CrudRepository<TargRepEsCell, Long>
{
    TargRepEsCell findTargRepEsCellById(Long id);
    TargRepEsCell findTargRepEsCellByName(String name);
    List<TargRepEsCell> findTargRepEsCellByAllele(TargRepAllele allele);
}
