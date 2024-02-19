package org.gentar.biology.targ_rep.es_cell;

import java.util.List;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * TargRepEsCellRepository.
 */
@Primary
public interface TargRepEsCellRepository extends PagingAndSortingRepository<TargRepEsCell, Long> {
    TargRepEsCell findTargRepEsCellById(Long id);

    List<TargRepEsCell> findAll();

    TargRepEsCell findTargRepEsCellByName(String name);

    List<TargRepEsCell> findTargRepEsCellByAllele(TargRepAllele allele);

    TargRepEsCell save(TargRepEsCell esCell);
}
