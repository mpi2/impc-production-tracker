package org.gentar.biology.targ_rep.mutation;

import java.util.List;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * TargRepEsCellService.
 */
public interface TargRepEsCellMutationService {

    TargRepEsCellMutation getTargRepEsCellMutationByEsCellId(Long esCellId);
}
