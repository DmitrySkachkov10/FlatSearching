package by.dmitryskachkov.flatservice.repo.api;

import by.dmitryskachkov.flatservice.repo.entity.Flat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IFlatRepo extends ListCrudRepository<Flat, String> {

    Page<Flat> findAll(Specification<Flat> flatEntitySpecification, Pageable pageable);
}

