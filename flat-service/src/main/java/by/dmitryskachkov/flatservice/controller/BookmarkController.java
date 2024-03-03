package by.dmitryskachkov.flatservice.controller;

import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.service.api.IBookmarkService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
    private final IBookmarkService bookmarkService;

    public BookmarkController(IBookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping()
    public ResponseEntity<PageOfFlat> getBookmarks(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "30") int size) {
        return new ResponseEntity<>(bookmarkService.getBookmarks(PageRequest.of(page-1, size)), HttpStatus.OK);
    }
}
