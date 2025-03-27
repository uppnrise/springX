package dev.bnacar.springx.data.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class for converting between entity and DTO objects.
 * Provides methods for converting single objects and collections.
 *
 * @param <E> the entity type
 * @param <D> the DTO type
 */
public class EntityDtoConverter<E, D> {

    private final Function<E, D> toDto;
    private final Function<D, E> toEntity;

    /**
     * Constructs a new EntityDtoConverter with the specified conversion functions.
     *
     * @param toDto the function to convert from entity to DTO
     * @param toEntity the function to convert from DTO to entity
     */
    public EntityDtoConverter(Function<E, D> toDto, Function<D, E> toEntity) {
        this.toDto = toDto;
        this.toEntity = toEntity;
    }

    /**
     * Converts an entity to a DTO.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    public D convertToDto(E entity) {
        if (entity == null) {
            return null;
        }
        return toDto.apply(entity);
    }

    /**
     * Converts a DTO to an entity.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    public E convertToEntity(D dto) {
        if (dto == null) {
            return null;
        }
        return toEntity.apply(dto);
    }

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param entities the entities to convert
     * @return the converted DTOs
     */
    public List<D> convertToDtoList(List<E> entities) {
        if (entities == null) {
            return null;
        }

        List<D> dtos = new ArrayList<>(entities.size());
        for (E entity : entities) {
            dtos.add(convertToDto(entity));
        }
        return dtos;
    }

    /**
     * Converts a list of DTOs to a list of entities.
     *
     * @param dtos the DTOs to convert
     * @return the converted entities
     */
    public List<E> convertToEntityList(List<D> dtos) {
        if (dtos == null) {
            return null;
        }

        List<E> entities = new ArrayList<>(dtos.size());
        for (D dto : dtos) {
            entities.add(convertToEntity(dto));
        }
        return entities;
    }
}
