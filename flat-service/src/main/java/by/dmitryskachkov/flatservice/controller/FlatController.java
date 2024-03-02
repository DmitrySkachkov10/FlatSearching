package by.dmitryskachkov.flatservice.controller;

import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.service.api.IBookmarkService;
import by.dmitryskachkov.flatservice.service.api.IFlatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/flats")
public class FlatController {

    private final IFlatService flatService;

    private final IBookmarkService bookmarkService;

    public FlatController(IFlatService flatService, IBookmarkService bookmarkService) {
        this.flatService = flatService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping
    public ResponseEntity<PageOfFlat> getFlats(FlatFilter flatFilter) {
        return new ResponseEntity<>(flatService.getPageOfFlat(flatFilter), HttpStatus.OK);
    }

    @GetMapping("/{uuid}/bookmark")
    public ResponseEntity<String> addBookMark(@PathVariable UUID uuid){
        bookmarkService.addBookmark(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}/bookmark")
    public ResponseEntity<String> deleteBookmark(@PathVariable UUID uuid){
        bookmarkService.deleteBookmarks(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
