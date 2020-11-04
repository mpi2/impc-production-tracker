package org.gentar.util.stream;

import org.springframework.data.jpa.domain.Specification;
import java.util.stream.Stream;

/**
 * Repository fragment that allows to add functionality to spring jpa repositories
 * without actually implementing it.
 * Taken from https://itnext.io/scrolling-through-large-datasets-in-spring-data-jpa-with-streams-and-specification-2fd975129758.
 * @param <T>
 */
public interface StreamableJpaSpecificationRepository<T>
{
    /**
     * must be used in a @Transaction
     * @param specification nullable; specification used by the query
     * @param clazz the class of the domain object {@link T}, necessary because we need the class info in the Impl
     * @return
     */
    Stream<T> stream(Specification<T> specification, Class<T> clazz);
}
