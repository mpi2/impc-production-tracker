package org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type;

import org.springframework.data.repository.CrudRepository;

public interface NucleaseTypeRepository extends CrudRepository<NucleaseType, Long>
{
    NucleaseType findByNameAndNucleaseClassNameIgnoreCase(String nucleaseType, String nucleaseClass);
}
