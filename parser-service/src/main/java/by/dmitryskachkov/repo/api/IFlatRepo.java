package by.dmitryskachkov.repo.api;

import by.dmitryskachkov.repo.entity.FlatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface IFlatRepo extends JpaRepository<FlatEntity, String> {
}
