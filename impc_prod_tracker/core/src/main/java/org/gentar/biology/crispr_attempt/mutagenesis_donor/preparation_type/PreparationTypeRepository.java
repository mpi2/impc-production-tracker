package org.gentar.biology.crispr_attempt.mutagenesis_donor.preparation_type;

import org.springframework.data.repository.CrudRepository;

public interface PreparationTypeRepository extends CrudRepository<PreparationType, Long>
{
    PreparationType findFirstByName(String name);
}
