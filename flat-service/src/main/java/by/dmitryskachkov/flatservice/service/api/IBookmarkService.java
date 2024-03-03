package by.dmitryskachkov.flatservice.service.api;



import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IBookmarkService {

    void addBookmark(UUID flatUuid);

    void deleteBookmarks(UUID flatUuid);

    PageOfFlat getBookmarks(Pageable pageable);
}
