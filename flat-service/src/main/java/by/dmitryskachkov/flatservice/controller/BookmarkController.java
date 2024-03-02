package by.dmitryskachkov.flatservice.controller;

import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.service.api.IBookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
    private final IBookmarkService bookmarkService;

    public BookmarkController(IBookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping()
    public ResponseEntity<PageOfFlat> getBookmarks() {
        return new ResponseEntity<>(bookmarkService.getBookmarks(), HttpStatus.OK);
    }
}
