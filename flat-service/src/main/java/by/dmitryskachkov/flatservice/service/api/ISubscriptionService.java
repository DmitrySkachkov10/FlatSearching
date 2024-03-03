package by.dmitryskachkov.flatservice.service.api;

import by.dmitryskachkov.flatservice.core.dto.PageOfSubscription;
import by.dmitryskachkov.flatservice.core.dto.SubscriptionDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ISubscriptionService {
    void addSubscription(SubscriptionDTO subscriptionDTO);
    void deleteSubscription(UUID subscriptionUuid, long updateTime);
    PageOfSubscription getSubscriptions(Pageable pageable);
    void updateSubscription(SubscriptionDTO subscriptionDTO, UUID subscriptionUuid, long updateTime);
}
