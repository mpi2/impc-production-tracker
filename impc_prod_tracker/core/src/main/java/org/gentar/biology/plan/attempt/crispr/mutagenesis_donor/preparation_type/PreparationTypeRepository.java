package org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.preparation_type;

import org.springframework.data.repository.CrudRepository;

public interface PreparationTypeRepository extends CrudRepository<PreparationType, Long>
{
    PreparationType findFirstByName(String name);
}
