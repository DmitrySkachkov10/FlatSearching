package by.dmitryskachkov.flatservice.core;

import by.dmitryskachkov.flatservice.core.dto.Flat;
import by.dmitryskachkov.flatservice.repo.entity.FlatEntity;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface EntityDtoMapper {
    EntityDtoMapper INSTANCE = Mappers.getMapper(EntityDtoMapper.class);
    FlatEntity flatDtoToFlat(Flat flat);

    Flat flatToFlatDto(FlatEntity flat);

    default List<Flat> flatListToFlatDTOList(List<FlatEntity> content) {
        return content.stream().map(INSTANCE::flatToFlatDto).toList();
    }

}
