package by.dmitryskachkov.flatservice.service;

import by.dmitryskachkov.entity.ValidationError;
import by.dmitryskachkov.flatservice.core.SubscriptionMapper;
import by.dmitryskachkov.flatservice.core.dto.*;
import by.dmitryskachkov.flatservice.core.filter.User;
import by.dmitryskachkov.flatservice.repo.api.ISubscriptionRepo;
import by.dmitryskachkov.flatservice.repo.entity.Subscription;
import by.dmitryskachkov.flatservice.service.api.IFlatService;
import by.dmitryskachkov.flatservice.service.api.IMailService;
import by.dmitryskachkov.flatservice.service.api.ISubscriptionService;
import by.dmitryskachkov.flatservice.service.api.IUserService;
import jakarta.transaction.Transactional;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService implements ISubscriptionService {

    private final ISubscriptionRepo subscriptionRepo;

    private final IFlatService flatService;

    private final IMailService mailService;

    @Value("${my.adminToken}")
    private  String adminToken;
    private final IUserService userService;

    public SubscriptionService(ISubscriptionRepo subscriptionRepo, IFlatService flatService, IMailService mailService, IUserService userService) {
        this.subscriptionRepo = subscriptionRepo;
        this.flatService = flatService;
        this.mailService = mailService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void addSubscription(SubscriptionDTO subscriptionDTO) {
        UserSecurity user = (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        UserSecurity user = (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        UserSecurity user = (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Subscription subscription = subscriptionRepo.findById(subscriptionUuid).orElse(null);

        LocalDateTime latestUpdateTime = subscription.getDtUpdate().truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime newUpdateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(updateTime), ZoneOffset.UTC);

        if (!latestUpdateTime.equals(newUpdateTime) || subscription == null) {
            throw new ValidationError("Запрос некорректен. Сервер не может обработать запрос");
        }

        subscription.setUuid(subscriptionUuid);
        subscription.setUserUuid(UUID.fromString(user.getUuid()));

        subscription.setAreaTo(subscriptionDTO.getAreaTo());
        subscription.setAreaFrom(subscriptionDTO.getAreaFrom());
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


    //todo можно добавить фильтр чтобы отсылать только актуальные данные за прошедшую неделю
    @Scheduled(cron = "0 0 0 * * MON")
    private void sendSubscription() {
        List<Subscription> subscriptions = subscriptionRepo.findAll();

        subscriptions.forEach(subscription -> {
            PageOfFlat pageOfFlat = flatService.getPageOfFlat(new FlatFilter(1, Integer.MAX_VALUE, subscription.getPriceFrom(), subscription.getPriceTo(), subscription.getBedroomsFrom(), subscription.getBedroomsTo(), subscription.getAreaFrom(), subscription.getAreaTo(), subscription.getFloors(), subscription.getPhoto()));
            User user = userService.getUserByUuid(subscription.getUserUuid(), adminToken);
            mailService.send(pageOfFlat, user.getMail());
        });
    }
}


