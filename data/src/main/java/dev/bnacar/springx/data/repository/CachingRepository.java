package dev.bnacar.springx.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base repository interface that adds caching capabilities to Spring Data JPA repositories.
 * Extends JpaRepository and adds @Cacheable annotation to common methods.
 *
 * @param <T> the domain type the repository manages
 * @param <ID> the type of the id of the entity the repository manages
 */
@NoRepositoryBean
public interface CachingRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Retrieves an entity by its id, with caching.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    @Override
    @Cacheable(ttlSeconds = 600)
    Optional<T> findById(ID id);

    /**
     * Returns all instances of the type, with caching.
     *
     * @return all entities
     */
    @Override
    @Cacheable(ttlSeconds = 300)
    List<T> findAll();

    /**
     * Returns the number of entities available, with caching.
     *
     * @return the number of entities
     */
    @Override
    @Cacheable(ttlSeconds = 300)
    long count();
}
