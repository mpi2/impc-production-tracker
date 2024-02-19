package org.gentar.biology.targ_rep.targeting_vector;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * TargRepTargetingVectorService.
 */
public interface TargRepTargetingVectorService {

    TargRepTargetingVector getNotNullTargRepTargetingVectorById(Long id);

    Page<TargRepTargetingVector> getPageableTargRepTargetingVector(Pageable page);

    TargRepTargetingVector getTargRepTargetingVectorByNameFailsIfNull(String name);

    TargRepTargetingVector save(TargRepTargetingVector targetingVector);
}
