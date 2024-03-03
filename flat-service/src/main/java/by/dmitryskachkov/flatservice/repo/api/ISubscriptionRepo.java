package by.dmitryskachkov.flatservice.repo.api;

import by.dmitryskachkov.flatservice.repo.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ISubscriptionRepo extends JpaRepository<Subscription, UUID> {


    Page<Subscription> findByUserUuid(UUID userUuid, Pageable pageable);

}
