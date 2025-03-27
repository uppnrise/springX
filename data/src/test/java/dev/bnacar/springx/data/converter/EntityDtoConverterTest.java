package dev.bnacar.springx.data.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class EntityDtoConverterTest {

    private EntityDtoConverter<TestEntity, TestDto> converter;

    @BeforeEach
    public void setup() {
        Function<TestEntity, TestDto> toDto = entity -> new TestDto(entity.getId(), entity.getName());
        Function<TestDto, TestEntity> toEntity = dto -> new TestDto(dto.getId(), dto.getName()).toEntity();

        converter = new EntityDtoConverter<>(toDto, toEntity);
    }

    @Test
    public void testConvertToDto() {
        // Arrange
        TestEntity entity = new TestEntity(1L, "Test Entity");

        // Act
        TestDto dto = converter.convertToDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Entity", dto.getName());
    }

    @Test
    public void testConvertToDtoWithNull() {
        // Arrange & Act
        TestDto dto = converter.convertToDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    public void testConvertToEntity() {
        // Arrange
        TestDto dto = new TestDto(1L, "Test DTO");

        // Act
        TestEntity entity = converter.convertToEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Test DTO", entity.getName());
    }

    @Test
    public void testConvertToEntityWithNull() {
        // Arrange & Act
        TestEntity entity = converter.convertToEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    public void testConvertToDtoList() {
        // Arrange
        List<TestEntity> entities = Arrays.asList(
                new TestEntity(1L, "Entity 1"),
                new TestEntity(2L, "Entity 2"),
                new TestEntity(3L, "Entity 3")
        );

        // Act
        List<TestDto> dtos = converter.convertToDtoList(entities);

        // Assert
        assertNotNull(dtos);
        assertEquals(3, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("Entity 1", dtos.get(0).getName());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("Entity 2", dtos.get(1).getName());
        assertEquals(3L, dtos.get(2).getId());
        assertEquals("Entity 3", dtos.get(2).getName());
    }

    @Test
    public void testConvertToDtoListWithNull() {
        // Arrange & Act
        List<TestDto> dtos = converter.convertToDtoList(null);

        // Assert
        assertNull(dtos);
    }

    @Test
    public void testConvertToEntityList() {
        // Arrange
        List<TestDto> dtos = Arrays.asList(
                new TestDto(1L, "DTO 1"),
                new TestDto(2L, "DTO 2"),
                new TestDto(3L, "DTO 3")
        );

        // Act
        List<TestEntity> entities = converter.convertToEntityList(dtos);

        // Assert
        assertNotNull(entities);
        assertEquals(3, entities.size());
        assertEquals(1L, entities.get(0).getId());
        assertEquals("DTO 1", entities.get(0).getName());
        assertEquals(2L, entities.get(1).getId());
        assertEquals("DTO 2", entities.get(1).getName());
        assertEquals(3L, entities.get(2).getId());
        assertEquals("DTO 3", entities.get(2).getName());
    }

    @Test
    public void testConvertToEntityListWithNull() {
        // Arrange & Act
        List<TestEntity> entities = converter.convertToEntityList(null);

        // Assert
        assertNull(entities);
    }

    // Test classes
    static class TestEntity {
        private final Long id;
        private final String name;

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    static class TestDto {
        private Long id;
        private String name;

        public TestDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public TestEntity toEntity() {
            return new TestEntity(id, name);
        }
    }
}

