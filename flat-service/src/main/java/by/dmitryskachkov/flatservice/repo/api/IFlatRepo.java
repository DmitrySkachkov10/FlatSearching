package by.dmitryskachkov.flatservice.repo.api;

import by.dmitryskachkov.flatservice.repo.entity.Flat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IFlatRepo extends ListCrudRepository<Flat, String> {

    Page<Flat> findAll(Specification<Flat> flatEntitySpecification, Pageable pageable);

    @Query(value = "SELECT * FROM flat.flat AS f JOIN flat.bookmarks AS b ON b.flat_uuid = f.uuid WHERE b.user_uuid = :userUuid", nativeQuery = true)
    Page<Flat> findFlatsByUserUuid(@Param("userUuid") UUID userUuid, Pageable pageable);

}


