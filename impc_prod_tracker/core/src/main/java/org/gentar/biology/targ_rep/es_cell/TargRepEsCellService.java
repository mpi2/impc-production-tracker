package org.gentar.biology.targ_rep.es_cell;

import java.util.List;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * TargRepEsCellService.
 */
public interface TargRepEsCellService {
    TargRepEsCell getTargRepEsCellById(Long id);

    TargRepEsCell getTargRepEsCellByNameFailsIfNull(String name);

    Page<TargRepEsCell> getPageableTargRepEsCell(Pageable page);

    List<TargRepEsCell> getTargRepEscellByAlleleFailsIfNull(TargRepAllele allele);
}
