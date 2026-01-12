package org.gentar.biology.mutation.categorizarion;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MutationCategorizationRepository extends CrudRepository<MutationCategorization, Long>
{

    @Query("SELECT mc FROM MutationCategorization mc " +
           "WHERE LOWER(mc.name) = LOWER(:name) " +
           "AND LOWER(mc.mutationCategorizationType.name) = LOWER(:type)")
    MutationCategorization findByNameAndMutationCategorizationTypeNameIgnoreCase(
        @Param("name") String name, @Param("type") String type);

    MutationCategorization findByNameIgnoreCase(String name);
}
