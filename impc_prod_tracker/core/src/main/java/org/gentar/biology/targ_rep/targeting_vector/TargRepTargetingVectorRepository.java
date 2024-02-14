package org.gentar.biology.targ_rep.targeting_vector;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * TargRepTargetingVectorRepository.
 */
public interface TargRepTargetingVectorRepository
    extends PagingAndSortingRepository<TargRepTargetingVector, Long> {

    List<TargRepTargetingVector> findAll();

    TargRepTargetingVector findTargRepTargetingVectorById(Long id);

    TargRepTargetingVector findTargRepTargetingVectorsByName(String name);

    TargRepTargetingVector save(TargRepTargetingVector targetingVector);
}
