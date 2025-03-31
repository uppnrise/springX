package dev.bnacar.springx.data.integration;

import dev.bnacar.springx.data.converter.EntityDtoConverter;
import dev.bnacar.springx.data.query.QueryBuilder;
import dev.bnacar.springx.data.repository.Cacheable;
import dev.bnacar.springx.data.repository.CacheableRepositoryAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {DataIntegrationTest.TestConfig.class})
public class DataIntegrationTest {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private EntityDtoConverter<TestEntity, TestDto> converter;

    @Test
    public void testCacheableRepository() {
        // Reset the counter
        testRepository.resetCounter();

        // First call should execute the method
        String result1 = testRepository.findById(1L);
        assertEquals("Entity with ID: 1", result1);
        assertEquals(1, testRepository.getCounter());

        // Second call with same ID should use cache
        String result2 = testRepository.findById(1L);
        assertEquals("Entity with ID: 1", result2);
        assertEquals(1, testRepository.getCounter()); // Counter should not increment

        // Call with different ID should execute the method
        String result3 = testRepository.findById(2L);
        assertEquals("Entity with ID: 2", result3);
        assertEquals(2, testRepository.getCounter());
    }

    @Test
    public void testQueryBuilder() {
        // Build a query
        QueryBuilder queryBuilder = QueryBuilder.select("id", "name")
                .from("users")
                .where("status = ?", "active")
                .and("created_at > ?", "2023-01-01")
                .orderBy("name ASC")
                .limit(10)
                .offset(0);

        // Verify the query
        String expectedQuery = "SELECT id, name FROM users WHERE status = ? AND created_at > ? ORDER BY name ASC LIMIT ? OFFSET ?";
        Object[] expectedParams = new Object[]{"active", "2023-01-01", 10, 0};

        assertEquals(expectedQuery, queryBuilder.getQuery());
        assertArrayEquals(expectedParams, queryBuilder.getParameters());
    }

    @Test
    public void testEntityDtoConverter() {
        // Create test entities
        List<TestEntity> entities = Arrays.asList(
                new TestEntity(1L, "Entity 1"),
                new TestEntity(2L, "Entity 2"),
                new TestEntity(3L, "Entity 3")
        );

        // Convert to DTOs
        List<TestDto> dtos = converter.convertToDtoList(entities);

        // Verify conversion
        assertEquals(3, dtos.size());
        assertEquals(1L, dtos.get(0).id());
        assertEquals("Entity 1", dtos.get(0).name());
        assertEquals(2L, dtos.get(1).id());
        assertEquals("Entity 2", dtos.get(1).name());
        assertEquals(3L, dtos.get(2).id());
        assertEquals("Entity 3", dtos.get(2).name());

        // Convert back to entities
        List<TestEntity> convertedEntities = converter.convertToEntityList(dtos);

        // Verify conversion
        assertEquals(3, convertedEntities.size());
        assertEquals(1L, convertedEntities.get(0).id());
        assertEquals("Entity 1", convertedEntities.get(0).name());
        assertEquals(2L, convertedEntities.get(1).id());
        assertEquals("Entity 2", convertedEntities.get(1).name());
        assertEquals(3L, convertedEntities.get(2).id());
        assertEquals("Entity 3", convertedEntities.get(2).name());
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableAspectJAutoProxy
    static class TestConfig {

        @Bean
        public TestRepository testRepository() {
            return new TestRepository();
        }

        @Bean
        public CacheableRepositoryAspect cacheableRepositoryAspect() {
            return new CacheableRepositoryAspect();
        }

        @Bean
        public EntityDtoConverter<TestEntity, TestDto> entityDtoConverter() {
            Function<TestEntity, TestDto> toDto = entity -> new TestDto(entity.id(), entity.name());
            Function<TestDto, TestEntity> toEntity = dto -> new TestEntity(dto.id(), dto.name());
            return new EntityDtoConverter<>(toDto, toEntity);
        }
    }

    @Repository
    static class TestRepository {

        private final AtomicInteger counter = new AtomicInteger(0);

        @Cacheable(ttlSeconds = 300)
        public String findById(Long id) {
            counter.incrementAndGet();
            return "Entity with ID: " + id;
        }

        public void resetCounter() {
            counter.set(0);
        }

        public int getCounter() {
            return counter.get();
        }
    }

    record TestEntity(Long id, String name) {
    }

    record TestDto(Long id, String name) {
    }
}

