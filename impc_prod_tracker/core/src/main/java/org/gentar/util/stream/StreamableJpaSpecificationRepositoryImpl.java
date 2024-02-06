package org.gentar.util.stream;

import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.stream.Stream;


/**
 * Taken from <a href="https://itnext.io/scrolling-through-large-datasets-in-spring-data-jpa-with-streams-and-specification-2fd975129758">...</a>.
 * @param <T>
 */
public class StreamableJpaSpecificationRepositoryImpl <T> implements StreamableJpaSpecificationRepository<T>
{
    @Autowired
    private EntityManager entityManager;

    @Override
    public Stream<T> stream(Specification<T> specification, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        // do some sanity checks for your pleasure
        if (specification != null) {
            criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
        }
        return entityManager.createQuery(criteriaQuery)
            .setHint(HINT_FETCH_SIZE, "1") // depends on your DB implementation; MySQL expects "" + Integer.MIN_VALUE
            .getResultStream();
    }
}
