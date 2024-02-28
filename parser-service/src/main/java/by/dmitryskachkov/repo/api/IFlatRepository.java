package by.dmitryskachkov.repo.api;

import by.dmitryskachkov.repo.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IFlatRepository extends JpaRepository<Flat, String> {
    Flat findByOriginalUrl(String originalUrl);
}
