package by.dmitryskachkov.flatservice.repo.api;

import by.dmitryskachkov.flatservice.repo.entity.FlatEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IFlatRepo extends ListCrudRepository<FlatEntity, UUID> {
}
