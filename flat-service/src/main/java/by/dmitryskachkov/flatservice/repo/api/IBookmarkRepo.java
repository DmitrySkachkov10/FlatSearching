package by.dmitryskachkov.flatservice.repo.api;

import by.dmitryskachkov.flatservice.repo.entity.Bookmark;
import org.springframework.data.repository.ListCrudRepository;


import java.util.UUID;

public interface IBookmarkRepo extends ListCrudRepository<Bookmark, UUID> {

    void deleteByFlatUuid(UUID flatUuid);


}
