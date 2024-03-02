package by.dmitryskachkov.flatservice.service;

import by.dmitryskachkov.flatservice.core.dto.FlatDTO;
import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.core.dto.UserSecurity;
import by.dmitryskachkov.flatservice.repo.api.IBookmarkRepo;
import by.dmitryskachkov.flatservice.repo.api.IFlatRepo;
import by.dmitryskachkov.flatservice.repo.entity.Bookmark;
import by.dmitryskachkov.flatservice.repo.entity.Flat;
import by.dmitryskachkov.flatservice.repo.entity.Photo;
import by.dmitryskachkov.flatservice.service.api.IBookmarkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookmarkService implements IBookmarkService {

    private final IFlatRepo flatRepo;

    private final IBookmarkRepo bookmarkRepo;

    public BookmarkService(IFlatRepo flatRepo, IBookmarkRepo bookmarkRepo) {
        this.flatRepo = flatRepo;
        this.bookmarkRepo = bookmarkRepo;
    }

    @Override
    @Transactional
    public void addBookmark(UUID flatUuid) {
        UserSecurity userSecurity = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        bookmarkRepo.save(new Bookmark(UUID.fromString(userSecurity.getUuid()), flatUuid));
    }

    @Override
    @Transactional
    public void deleteBookmarks(UUID flatUuid) {
        bookmarkRepo.deleteByFlatUuid(flatUuid);
    }

    @Override
    public PageOfFlat getBookmarks() {
        UserSecurity userSecurity = (UserSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Page<Flat> flatPage = flatRepo.findFlatsByUserUuid(UUID.fromString(userSecurity.getUuid()), PageRequest.of(0,50));
        //todo не указано в openApi  номера страниц и кол-во элементов!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        List<Flat> flats = flatPage.getContent();
        List<FlatDTO> flatDTOList = new ArrayList<>();

        for (Flat flat : flats) {
            FlatDTO flatDTO = new FlatDTO();

            flatDTO.setOriginalUrl(flat.getOriginalUrl());
            flatDTO.setUuid(flat.getUuid());
            flatDTO.setDtCreate(flat.getCreateDate());
            flatDTO.setDtUpdate(flat.getUpdateDate());
            flatDTO.setOfferType(flat.getOfferType());
            flatDTO.setDescription(flat.getDescription());
            flatDTO.setFloor(flat.getFloor());
            flatDTO.setBedrooms(flat.getBedrooms());
            flatDTO.setPrice(flat.getPrice());
            flatDTO.setArea(flat.getArea());

            if (flat.getPhotos() != null) {
                flatDTO.setPhotos(flat.getPhotos().stream()
                        .map(Photo::getPhotoUrl)
                        .collect(Collectors.toSet()));
            }

            flatDTOList.add(flatDTO);
        }

        return new PageOfFlat(flatPage.getNumber(), flatPage.getSize(), flatPage.getTotalPages(),
                flatPage.getTotalElements(), flatPage.isFirst(), flatPage.getNumberOfElements(),
                flatPage.isLast(), flatDTOList);
    }
}
