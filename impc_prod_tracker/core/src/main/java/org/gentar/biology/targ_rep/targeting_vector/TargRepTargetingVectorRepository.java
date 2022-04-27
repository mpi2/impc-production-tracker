package org.gentar.biology.targ_rep.targeting_vector;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * TargRepTargetingVectorRepository.
 */
public interface TargRepTargetingVectorRepository
    extends PagingAndSortingRepository<TargRepTargetingVector, Long> {

    List<TargRepTargetingVector> findAll();

    TargRepTargetingVector findTargRepTargetingVectorById(Long id);
}
