package by.dmitryskachkov.flatservice.service;


import by.dmitryskachkov.flatservice.core.dto.FlatDTO;
import by.dmitryskachkov.flatservice.core.dto.FlatFilter;
import by.dmitryskachkov.flatservice.core.dto.PageOfFlat;
import by.dmitryskachkov.flatservice.repo.api.IFlatRepo;
import by.dmitryskachkov.flatservice.repo.entity.Flat;
import by.dmitryskachkov.flatservice.repo.entity.Photo;
import by.dmitryskachkov.flatservice.service.api.IFlatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlatService implements IFlatService {

    private IFlatRepo flatRepo;


    public FlatService(IFlatRepo flatRepo, EntityManager entityManager) {
        this.flatRepo = flatRepo;
    }

    @Override
    public PageOfFlat getPageOfFlat(FlatFilter flatFilter) {
        Specification<Flat> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.between(root.get("price"), flatFilter.getPriceFrom(), flatFilter.getPriceTo()));
            predicates.add(cb.between(root.get("bedrooms"), flatFilter.getBedroomsFrom(), flatFilter.getBedroomsTo()));
            predicates.add(cb.between(root.get("area"), flatFilter.getAreaFrom(), flatFilter.getAreaTo()));

            if (flatFilter.isPhoto()) {
                predicates.add(cb.isNotEmpty(root.get("photos")));
            }
            if (flatFilter.getFloors() != null) {
                predicates.add(root.get("floor").in(flatFilter.getFloors()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(flatFilter.getPage(), flatFilter.getSize());
        Page<Flat> resultPage = flatRepo.findAll(specification, pageable);

        List<Flat> flats = resultPage.getContent();
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

        return new PageOfFlat(resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalPages(),
                resultPage.getTotalElements(), resultPage.isFirst(), resultPage.getNumberOfElements(),
                resultPage.isLast(), flatDTOList);
    }


}
