package by.dmitryskachkov.repo.api;

import by.dmitryskachkov.repo.entity.Flat;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface IFlatRepository extends JpaRepository<Flat, String> {
    Flat findByOriginalUrl(String originalUrl);


}
