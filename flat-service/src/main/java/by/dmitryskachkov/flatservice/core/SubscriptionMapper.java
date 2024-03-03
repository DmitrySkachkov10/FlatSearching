package by.dmitryskachkov.flatservice.core;

import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.SubscriptionDTO;
import by.dmitryskachkov.flatservice.repo.entity.Subscription;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SubscriptionMapper {

    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);


    SubscriptionDTO mapToSubscriptionDTO(FlatFilter flatFilter);

    @AfterMapping
    default void mapUserUuidAndUuid(@MappingTarget SubscriptionDTO subscriptionDTO, FlatFilter flatFilter) {
        subscriptionDTO.setUserUuid(null);
        subscriptionDTO.setUuid(null);
        subscriptionDTO.setDtCreate(LocalDateTime.now());
        subscriptionDTO.setDtUpdate(LocalDateTime.now());}

    default List<SubscriptionDTO> mapToSubscriptionDTOList(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(this::subscriptionToSubscriptionDto)
                .collect(Collectors.toList());
    }

    Subscription subscriptionDtoToSubscription(SubscriptionDTO subscriptionDto);

    SubscriptionDTO subscriptionToSubscriptionDto(Subscription subscription);
}