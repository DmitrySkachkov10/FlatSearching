package by.dmitryskachkov.flatservice.service;

import by.dmitryskachkov.entity.ValidationError;
import by.dmitryskachkov.flatservice.core.SubscriptionMapper;
import by.dmitryskachkov.flatservice.core.dto.PageOfSubscription;
import by.dmitryskachkov.flatservice.core.dto.SubscriptionDTO;
import by.dmitryskachkov.flatservice.core.dto.UserSecurity;
import by.dmitryskachkov.flatservice.repo.api.ISubscriptionRepo;
import by.dmitryskachkov.flatservice.repo.entity.Subscription;
import by.dmitryskachkov.flatservice.service.api.ISubscriptionService;
import jakarta.transaction.Transactional;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class SubscriptionService implements ISubscriptionService {

    private final ISubscriptionRepo subscriptionRepo;

    public SubscriptionService(ISubscriptionRepo subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    @Override
    @Transactional
    public void addSubscription(SubscriptionDTO subscriptionDTO) {
        UserSecurity user = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        subscriptionDTO.setDtCreate(LocalDateTime.now());
        subscriptionDTO.setDtUpdate(LocalDateTime.now());
        subscriptionDTO.setUserUuid(UUID.fromString(user.getUuid()));
        subscriptionDTO.setUuid(UUID.randomUUID());

        Subscription subscription = SubscriptionMapper.INSTANCE.subscriptionDtoToSubscription(subscriptionDTO);

        subscriptionRepo.save(subscription);
    }

    @Override
    @Transactional
    public void deleteSubscription(UUID subscriptionUuid, long updateTime) {

        Subscription oldSubscription = subscriptionRepo.findById(subscriptionUuid).orElse(null);

        if (oldSubscription == null) {
            throw new ValidationError("Запрос некорректен. Сервер не может обработать запрос");
        }

        LocalDateTime latestUpdateTime = oldSubscription.getDtUpdate().truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime newUpdateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(updateTime), ZoneOffset.UTC);

        if (latestUpdateTime.equals(newUpdateTime)) {
            subscriptionRepo.deleteById(subscriptionUuid);
        } else {
            throw new ValidationError("Запрос некорректен. Сервер не может обработать запрос");
        }
    }

    @Override
    public PageOfSubscription getSubscriptions(Pageable pageable) {
        UserSecurity user = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Page<Subscription> subscriptions = subscriptionRepo.findByUserUuid(UUID.fromString(user.getUuid()), pageable);

        PageOfSubscription pageOfSubscription = new PageOfSubscription();
        pageOfSubscription.setLast(subscriptions.isLast());
        pageOfSubscription.setFirst(subscriptions.isFirst());
        pageOfSubscription.setTotal_pages(subscriptions.getTotalPages());
        pageOfSubscription.setSize(subscriptions.getSize());
        pageOfSubscription.setNumber_of_elements(subscriptions.getNumberOfElements());
        pageOfSubscription.setTotal_elements(subscriptions.getTotalElements());
        pageOfSubscription.setContent(SubscriptionMapper.INSTANCE.mapToSubscriptionDTOList(subscriptions.getContent()));

        return pageOfSubscription;
    }

    @Override
    @Transactional
    public void updateSubscription(SubscriptionDTO subscriptionDTO, UUID subscriptionUuid, long updateTime) {
        UserSecurity user = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Subscription subscription = subscriptionRepo.findById(subscriptionUuid).orElse(null);

        LocalDateTime latestUpdateTime = subscription.getDtUpdate().truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime newUpdateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(updateTime), ZoneOffset.UTC);

        if (!latestUpdateTime.equals(newUpdateTime) || subscription == null) {
            throw new ValidationError("Запрос некорректен. Сервер не может обработать запрос");
        }

        subscription.setUuid(subscriptionUuid);
        subscription.setUserUuid(UUID.fromString(user.getUuid()));

        subscription.setAreaTo(subscriptionDTO.getAreaTo());
        subscription.setAreaFrom(subscriptionDTO.getAreaFrom())
        ;
        subscription.setPhoto(subscriptionDTO.isPhoto());
        subscription.setFloors(subscriptionDTO.getFloors());

        subscription.setDtUpdate(LocalDateTime.now());

        subscription.setBedroomsFrom(subscriptionDTO.getBedroomsFrom());
        subscription.setBedroomsTo(subscriptionDTO.getBedroomsTo());

        subscription.setPriceFrom(subscription.getBedroomsFrom());
        subscription.setPriceTo(subscriptionDTO.getPriceTo());

        try {
            subscriptionRepo.save(subscription);
        } catch (OptimisticEntityLockException e) {
            throw new ValidationError("Запрос некорректен. Сервер не может обработать запрос");
        }
    }

}


