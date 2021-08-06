package org.gentar.biology.targ_rep.es_cell;

import org.gentar.biology.targ_rep.allele.TargRepAllele;

import java.util.List;

public interface TargRepEsCellService
{
    TargRepEsCell getTargRepEsCellById(Long id);

    TargRepEsCell getTargRepEsCellByNameFailsIfNull(String name);

    List<TargRepEsCell> getTargRepEscellByAlleleFailsIfNull(TargRepAllele allele);
}
